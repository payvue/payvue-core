package com.payvue.core.community;

import java.util.UUID;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.common.status.EngagementState;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class Community
        extends AggregateRoot<CommunityId, Community> implements Identifiable<CommunityId>, CommunityCapability {

    @Getter
    private final CommunityId id;
    @Getter
    private String name;
    private String description;
    private final Set<Participant> participants;

    protected Community(
            String name,
            String description,
            UUID adminId,
            RoleDescriptor adminRole
    ) {
        this(CommunityId.generate(), name, description, adminId, adminRole);
    }

    protected Community(
            CommunityId id,
            String name,
            String description,
            UUID adminId,
            RoleDescriptor adminRole
    ) {
        this.id = Objects.requireNonNull(id, "id는 필수입니다.");
        this.name = name;
        this.description = description;
        this.participants = new HashSet<>();

        this.participants.add(Participant.of(adminId, adminRole, EngagementState.ACTIVE));
        validateInvariants();
    }

    public abstract String getCommunityType();

    public static Participant to(Community community, UUID participantId) {
        RoleDescriptor role = community.getParticipantRole(participantId);
        return Participant.of(participantId, role);
    }

    @Override
    public boolean isParticipant(UUID userId) {
        return findParticipant(userId).isPresent();
    }

    @Override
    public boolean isAdmin(UUID userId) {
        return findParticipant(userId)
                .map(Participant::isAdmin)
                .orElse(false);
    }

    @Override
    public RoleDescriptor getParticipantRole(UUID userId) {
        return findParticipant(userId)
                .map(Participant::getRole)
                .orElseThrow(() -> new IllegalArgumentException("참여자를 찾을 수 없습니다."));
    }

    @Override
    public Set<UUID> getParticipantIds() {
        return participants.stream()
                .map(Participant::getUserId)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public EngagementState getParticipantState(UUID participantId) {
        return participants.stream()
                .filter(participant -> participant.getUserId().equals(participantId))
                .findFirst()
                .map(participant -> participant.getEngagementState())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public int getParticipantCount() {
        return participants.size();
    }

    // 내부에서만 사용하는 메서드
    protected Optional<Participant> findParticipant(UUID userId) {
        return participants.stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst();
    }

    protected Set<Participant> getParticipantsInternal() {
        return participants;
    }

    protected void validateInvariants() {
        if (participants.isEmpty()) {
            throw new IllegalStateException("커뮤니티는 최소 한 명 이상의 구성원이 필요합니다.");
        }

        boolean hasAdmin = participants.stream()
                .anyMatch(p -> p.getRole().isAdmin());

        if (!hasAdmin) {
            throw new IllegalStateException("커뮤니티에는 최소 한 명 이상의 결정권자(ADMIN)가 필요합니다.");
        }
    }

    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("커뮤니티 이름은 비어 있을 수 없습니다.");
        }
        this.name = name.trim();
    }

    public void changeDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
