package com.payvue.supporting.user.command;

import lombok.Getter;

/**
 * UserStatus - 사용자 상태 열거형
 *
 * 각 상태는 전이 가능한 다음 상태들을 알고 있으며,
 * 전이 불가능한 경우 예외를 발생시킵니다.
 */
@Getter
public enum UserStatus {

    /**
     * 가입 대기
     */
    PENDING("가입 대기") {
        @Override
        public boolean canTransitionTo(UserStatus newStatus) {
            return newStatus == ACTIVE;
        }

        @Override
        public UserStatus transitionTo(UserStatus newStatus) {
            if (newStatus == ACTIVE) {
                return newStatus;
            }
            throw new IllegalStateTransitionException(this, newStatus);
        }

        @Override
        public boolean isPending() {
            return true;
        }
    },

    /**
     * 활성
     */
    ACTIVE("활성") {
        @Override
        public boolean canTransitionTo(UserStatus newStatus) {
            return newStatus == SUSPENDED || newStatus == WITHDRAWN;
        }

        @Override
        public UserStatus transitionTo(UserStatus newStatus) {
            if (newStatus == SUSPENDED || newStatus == WITHDRAWN) {
                return newStatus;
            }
            throw new IllegalStateTransitionException(this, newStatus);
        }

        @Override
        public boolean isActive() {
            return true;
        }
    },

    /**
     * 정지
     */
    SUSPENDED("정지") {
        @Override
        public boolean canTransitionTo(UserStatus newStatus) {
            return newStatus == ACTIVE || newStatus == WITHDRAWN;
        }

        @Override
        public UserStatus transitionTo(UserStatus newStatus) {
            if (newStatus == ACTIVE || newStatus == WITHDRAWN) {
                return newStatus;
            }
            throw new IllegalStateTransitionException(this, newStatus);
        }

        @Override
        public boolean isSuspended() {
            return true;
        }
    },

    /**
     * 탈퇴
     */
    WITHDRAWN("탈퇴") {
        @Override
        public boolean canTransitionTo(UserStatus newStatus) {
            return false;
        }

        @Override
        public UserStatus transitionTo(UserStatus newStatus) {
            throw new IllegalStateTransitionException(
                this,
                newStatus,
                "탈퇴 상태에서는 전이할 수 없습니다"
            );
        }

        @Override
        public boolean isWithdrawn() {
            return true;
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    };

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    /**
     * 특정 상태로 전이 가능한지 확인
     */
    public abstract boolean canTransitionTo(UserStatus newStatus);

    /**
     * 특정 상태로 전이 시도
     *
     * @param newStatus 전이할 상태
     * @return 새로운 상태
     * @throws IllegalStateTransitionException 전이 불가능한 경우
     */
    public abstract UserStatus transitionTo(UserStatus newStatus);

    /**
     * 가입 대기 상태인가?
     */
    public boolean isPending() {
        return false;
    }

    /**
     * 활성 상태인가?
     */
    public boolean isActive() {
        return false;
    }

    /**
     * 정지 상태인가?
     */
    public boolean isSuspended() {
        return false;
    }

    /**
     * 탈퇴 상태인가?
     */
    public boolean isWithdrawn() {
        return false;
    }

    /**
     * 최종 상태인가?
     */
    public boolean isFinal() {
        return false;
    }

    /**
     * IllegalStateTransitionException - 상태 전이 예외
     */
    public static class IllegalStateTransitionException extends RuntimeException {

        public IllegalStateTransitionException(
            UserStatus from,
            UserStatus to
        ) {
            super(String.format(
                "사용자 상태 전이 불가능: %s -> %s",
                from,
                to
            ));
        }

        public IllegalStateTransitionException(
            UserStatus from,
            UserStatus to,
            String reason
        ) {
            super(String.format(
                "사용자 상태 전이 불가능: %s -> %s (%s)",
                from,
                to,
                reason
            ));
        }
    }
}
