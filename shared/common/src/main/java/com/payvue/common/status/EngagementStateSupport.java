package com.payvue.common.status;

/**
 * EngagementStateSupport - 참여 상태 지원 인터페이스
 * 
 * EngagementState를 가진 엔티티가 상태 전이 및 판단 로직을 
 * 재사용할 수 있도록 default 메서드를 제공합니다.
 */
public interface EngagementStateSupport {
    
    /**
     * 현재 참여 상태
     */
    EngagementState getEngagementState();
    void setEngagementState(EngagementState state);
    
    /**
     * 초대 대기 상태인가?
     */
    default boolean isPending() {
        return getEngagementState().isPending();
    }
    
    /**
     * 활성 상태인가?
     */
    default boolean isActive() {
        return getEngagementState().isActive();
    }
    
    /**
     * 정지 상태인가?
     */
    default boolean isSuspended() {
        return getEngagementState().isSuspended();
    }
    
    /**
     * 초대 거절 상태인가?
     */
    default boolean isDeclined() {
        return getEngagementState().isDeclined();
    }
    
    /**
     * 제거된 상태인가?
     */
    default boolean isRemoved() {
        return getEngagementState().isRemoved();
    }
    
    /**
     * 참여 가능한 상태인가?
     */
    default boolean canParticipate() {
        return getEngagementState().canParticipate();
    }
    
    /**
     * 최종 상태인가? (더 이상 변경 불가)
     */
    default boolean isFinal() {
        return getEngagementState().isFinal();
    }
    

    default boolean canTransitionTo(EngagementState newState) {
        return getEngagementState().canTransitionTo(newState);
    }

    default void pending() {
        EngagementState newState = getEngagementState().transitionTo(EngagementState.PENDING);
        setEngagementState(newState);
    }

    default void activate() {
        EngagementState newState = getEngagementState().transitionTo(EngagementState.ACTIVE);
        setEngagementState(newState);
    }

    default void suspend() {
        EngagementState newState = getEngagementState().transitionTo(EngagementState.SUSPENDED);
        setEngagementState(newState);
    }

    default void decline() {
        EngagementState newState = getEngagementState().transitionTo(EngagementState.DECLINED);
        setEngagementState(newState);
    }

    default void remove() {
        EngagementState newState = getEngagementState().transitionTo(EngagementState.REMOVED);
        setEngagementState(newState);
    }
}
