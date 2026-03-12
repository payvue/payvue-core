package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/**
 * CommunityMembership - 커뮤니티 멤버십 값 객체
 * 
 * 사용자가 가입한 커뮤니티 정보를 담습니다.
 */
@Getter
@EqualsAndHashCode
class Membership {

    private final String communityId;
    private final Instant joinedAt;

    private Membership(String communityId, Instant joinedAt) {
        validateCommunityId(communityId);
        this.communityId = communityId;
        this.joinedAt = joinedAt != null ? joinedAt : Instant.now();
    }

    public static Membership of(String communityId) {
        return new Membership(communityId, Instant.now());
    }

    private void validateCommunityId(String communityId) {
        if (communityId == null || communityId.isBlank()) {
            throw new IllegalArgumentException("커뮤니티 ID는 필수입니다.");
        }
    }
}
