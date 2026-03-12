package com.payvue.common.status;

/**
 * InteractionStatus - 상호작용 상태
 * 
 * 각 상태는 전이 가능한 다음 상태들을 알고 있으며,
 * 전이 불가능한 경우 예외를 발생시킵니다.
 */
public enum ExecutionState {
    
    /**
     * 승인 대기 (검증 대기)
     */
    PENDING_APPROVAL {
        @Override
        public boolean canTransitionTo(ExecutionState newStatus) {
            return true;
        }
        
        @Override
        public ExecutionState transitionTo(ExecutionState newStatus) {
            return newStatus;
        }
        
        @Override
        public boolean isPendingApproval() {
            return true;
        }
        
        @Override
        public boolean canExecute() {
            return true;
        }
    },
    
    /**
     * 승인됨 (실행 가능)
     */
    APPROVED {
        @Override
        public boolean canTransitionTo(ExecutionState newStatus) {
            return newStatus == COMPLETED || newStatus == FAILED;
        }
        
        @Override
        public ExecutionState transitionTo(ExecutionState newStatus) {
            if (newStatus == COMPLETED || newStatus == FAILED) {
                return newStatus;
            }
            throw new IllegalStateTransitionException(this, newStatus);
        }
        
        @Override
        public boolean isApproved() {
            return true;
        }
        
        @Override
        public boolean canExecute() {
            return true;
        }
    },
    
    /**
     * 거절됨 (검증 실패)
     */
    REJECTED {
        @Override
        public boolean canTransitionTo(ExecutionState newStatus) {
            return false;  // 최종 상태
        }
        
        @Override
        public ExecutionState transitionTo(ExecutionState newStatus) {
            throw new IllegalStateTransitionException(this, newStatus, "최종 상태에서는 전이할 수 없습니다");
        }
        
        @Override
        public boolean isRejected() {
            return true;
        }
        
        @Override
        public boolean isFinal() {
            return true;
        }
    },
    
    /**
     * 완료됨 (실행 성공)
     */
    COMPLETED {
        @Override
        public boolean canTransitionTo(ExecutionState newStatus) {
            return false;  // 최종 상태
        }
        
        @Override
        public ExecutionState transitionTo(ExecutionState newStatus) {
            throw new IllegalStateTransitionException(this, newStatus, "최종 상태에서는 전이할 수 없습니다");
        }
        
        @Override
        public boolean isCompleted() {
            return true;
        }
        
        @Override
        public boolean isFinal() {
            return true;
        }
    },
    
    /**
     * 실패함 (실행 실패)
     */
    FAILED {
        @Override
        public boolean canTransitionTo(ExecutionState newStatus) {
            return false;  // 최종 상태
        }
        
        @Override
        public ExecutionState transitionTo(ExecutionState newStatus) {
            throw new IllegalStateTransitionException(this, newStatus, "최종 상태에서는 전이할 수 없습니다");
        }
        
        @Override
        public boolean isFailed() {
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
    public abstract boolean canTransitionTo(ExecutionState newStatus);
    
    /**
     * 특정 상태로 전이 시도
     * 
     * @param newStatus 전이할 상태
     * @return 새로운 상태
     * @throws IllegalStateTransitionException 전이 불가능한 경우
     */
    public abstract ExecutionState transitionTo(ExecutionState newStatus);
    
    /**
     * 승인 대기 상태인가?
     */
    public boolean isPendingApproval() {
        return false;
    }
    
    /**
     * 승인된 상태인가?
     */
    public boolean isApproved() {
        return false;
    }
    
    /**
     * 거절된 상태인가?
     */
    public boolean isRejected() {
        return false;
    }
    
    /**
     * 완료된 상태인가?
     */
    public boolean isCompleted() {
        return false;
    }
    
    /**
     * 실패한 상태인가?
     */
    public boolean isFailed() {
        return false;
    }
    
    /**
     * 실행 가능한 상태인가?
     */
    public boolean canExecute() {
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
            ExecutionState from,
            ExecutionState to
        ) {
            super(String.format(
                "상태 전이 불가능: %s -> %s", 
                from, 
                to
            ));
        }
        
        public IllegalStateTransitionException(
            ExecutionState from,
            ExecutionState to,
            String reason
        ) {
            super(String.format(
                "상태 전이 불가능: %s -> %s (%s)", 
                from, 
                to,
                reason
            ));
        }
    }
}
