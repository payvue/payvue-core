package com.payvue.core.community;

import com.payvue.common.status.EngagementState;
import com.payvue.common.status.EngagementStateSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "userId")
public class Participant implements EngagementStateSupport {

    private final UUID id;
    private final UUID userId;
    private RoleDescriptor role;
    @Setter
    private EngagementState engagementState;

    private Participant(UUID id, UUID userId, RoleDescriptor role, EngagementState engagementState) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.engagementState = engagementState;
    }

    private Participant(UUID userId, RoleDescriptor role) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.role = role;
        this.engagementState = EngagementState.PENDING;
    }

    public static Participant of(UUID userId, RoleDescriptor role) {  // package-private
        return new Participant(userId, role);
    }

    public static Participant of(UUID userId, RoleDescriptor role, EngagementState state) {  // package-private
        return new Participant(UUID.randomUUID(), userId, role, state);
    }

    /**
     * 기존 데이터로부터 참여자를 복원합니다. (Repository용)
     */
    public static Participant reconstitute(UUID id, UUID userId, RoleDescriptor role,
                                    EngagementState state) {
        Participant participant = new Participant(id, userId, role, state);
        return participant;
    }

    public boolean isAdmin() {  // package-private
        return role.isAdmin();
    }

}
