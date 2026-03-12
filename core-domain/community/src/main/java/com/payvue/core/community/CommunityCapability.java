package com.payvue.core.community;

import java.util.UUID;

import com.payvue.core.community.policy.CommunityPolicy;

import java.util.Set;

interface CommunityCapability {

    boolean isParticipant(UUID userId);
    boolean isAdmin(UUID userId);
    RoleDescriptor getParticipantRole(UUID userId);
    Set<UUID> getParticipantIds();
    int getParticipantCount();

    default void addMember(UUID userId, CommunityPolicy policy) {
        throw new UnsupportedOperationException("개인 커뮤니티는 멤버를 추가할 수 없습니다.");
    }

    default void addMember(Participant participant, CommunityPolicy policy) {
        throw new UnsupportedOperationException("개인 커뮤니티는 멤버를 추가할 수 없습니다.");
    }

    default void removeMember(UUID userId, CommunityPolicy policy) {
        throw new UnsupportedOperationException("개인 커뮤니티는 멤버를 제거할 수 없습니다.");
    }

    default void changeParticipantRole(UUID userId, RoleDescriptor newRole, CommunityPolicy policy) {
        throw new UnsupportedOperationException("개인 커뮤니티는 역할을 변경할 수 없습니다.");
    }
}
