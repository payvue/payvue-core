package com.payvue.common.status;

/**
 * EngagementState - 커뮤니티 참여 상태
 * 
 * 커뮤니티에 속한 멤버의 참여 상태를 표현합니다.
 * 각 상태는 전이 가능한 다음 상태들을 알고 있으며,
 * 전이 불가능한 경우 예외를 발생시킵니다.
 */
public enum EngagementState {
    
    /**
     * 초대 대기 (초대 승인 대기 중)
     */
    PENDING {
        @Override
        public boolean canTransitionTo(EngagementState newState) {
            return newState == ACTIVE || newState == DECLINED;
        }
        
        @Override
        public EngagementState transitionTo(EngagementState newState) {
            if (newState == PENDING || newState == ACTIVE || newState == DECLINED) {
                return newState;
            }
            throw new IllegalStateTransitionException(this, newState);
        }
        
        @Override
        public boolean isPending() {
            return true;
        }
    },
    
    /**
     * 활성 (정상적으로 활동 중)
     */
    ACTIVE {
        @Override
        public boolean canTransitionTo(EngagementState newState) {
            return newState == SUSPENDED || newState == REMOVED;
        }
        
        @Override
        public EngagementState transitionTo(EngagementState newState) {
            if (newState == SUSPENDED || newState == REMOVED) {
                return newState;
            }
            throw new IllegalStateTransitionException(this, newState);
        }
        
        @Override
        public boolean isActive() {
            return true;
        }
        
        @Override
        public boolean canParticipate() {
            return true;
        }
    },
    
    /**
     * 정지됨 (일시적 활동 제한)
     */
    SUSPENDED {
        @Override
        public boolean canTransitionTo(EngagementState newState) {
            return newState == ACTIVE || newState == REMOVED;
        }
        
        @Override
        public EngagementState transitionTo(EngagementState newState) {
            if (newState == ACTIVE || newState == REMOVED) {
                return newState;
            }
            throw new IllegalStateTransitionException(this, newState);
        }
        
        @Override
        public boolean isSuspended() {
            return true;
        }
    },
    
    /**
     * 초대 거절 (초대를 거절함)
     */
    DECLINED {
        @Override
        public boolean canTransitionTo(EngagementState newState) {
            return false;  // 최종 상태
        }
        
        @Override
        public EngagementState transitionTo(EngagementState newState) {
            throw new IllegalStateTransitionException(
                this, 
                newState, 
                "초대 거절 상태에서는 전이할 수 없습니다"
            );
        }
        
        @Override
        public boolean isDeclined() {
            return true;
        }
        
        @Override
        public boolean isFinal() {
            return true;
        }
    },
    
    /**
     * 제거됨 (커뮤니티에서 제거됨)
     */
    REMOVED {
        @Override
        public boolean canTransitionTo(EngagementState newState) {
            return false;  // 최종 상태
        }
        
        @Override
        public EngagementState transitionTo(EngagementState newState) {
            throw new IllegalStateTransitionException(
                this, 
                newState, 
                "제거된 상태에서는 전이할 수 없습니다"
            );
        }
        
        @Override
        public boolean isRemoved() {
            return true;
        }
        
        @Override
        public boolean isFinal() {
            return true;
        }
    };
    
    /**
     * 특정 상태로 전이 가능한지 확인
     */
    public abstract boolean canTransitionTo(EngagementState newState);
    
    /**
     * 특정 상태로 전이 시도
     * 
     * @param newState 전이할 상태
     * @return 새로운 상태
     * @throws IllegalStateTransitionException 전이 불가능한 경우
     */
    public abstract EngagementState transitionTo(EngagementState newState);
    
    /**
     * 초대 대기 상태인가?
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
     * 초대 거절 상태인가?
     */
    public boolean isDeclined() {
        return false;
    }
    
    /**
     * 제거된 상태인가?
     */
    public boolean isRemoved() {
        return false;
    }
    
    /**
     * 참여 가능한 상태인가?
     */
    public boolean canParticipate() {
        return false;
    }
    
    /**
     * 최종 상태인가? (더 이상 변경 불가)
     */
    public boolean isFinal() {
        return false;
    }
    
    /**
     * IllegalStateTransitionException - 상태 전이 예외
     */
    public static class IllegalStateTransitionException extends RuntimeException {
        
        public IllegalStateTransitionException(
            EngagementState from,
            EngagementState to
        ) {
            super(String.format(
                "참여 상태 전이 불가능: %s -> %s", 
                from, 
                to
            ));
        }
        
        public IllegalStateTransitionException(
            EngagementState from,
            EngagementState to,
            String reason
        ) {
            super(String.format(
                "참여 상태 전이 불가능: %s -> %s (%s)", 
                from, 
                to,
                reason
            ));
        }
    }
}
