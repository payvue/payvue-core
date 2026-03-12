package com.payvue.supporting.user.command;

/**
 * UserStatusSupport - 사용자 상태 지원 인터페이스
 *
 * UserStatus를 가진 엔티티가 상태 전이 및 판단 로직을
 * 재사용할 수 있도록 default 메서드를 제공합니다.
 */
interface UserStatusSupport {

    /**
     * 현재 사용자 상태
     */
    UserStatus getStatus();
    void setStatus(UserStatus status);

    /**
     * 가입 대기 상태인가?
     */
    default boolean isPending() {
        return getStatus().isPending();
    }

    /**
     * 활성 상태인가?
     */
    default boolean isActive() {
        return getStatus().isActive();
    }

    /**
     * 정지 상태인가?
     */
    default boolean isSuspended() {
        return getStatus().isSuspended();
    }

    /**
     * 탈퇴 상태인가?
     */
    default boolean isWithdrawn() {
        return getStatus().isWithdrawn();
    }

    /**
     * 최종 상태인가?
     */
    default boolean isFinal() {
        return getStatus().isFinal();
    }

    default boolean canTransitionTo(UserStatus newStatus) {
        return getStatus().canTransitionTo(newStatus);
    }

    default void activate() {
        UserStatus newStatus = getStatus().transitionTo(UserStatus.ACTIVE);
        setStatus(newStatus);
    }

    default void suspend() {
        UserStatus newStatus = getStatus().transitionTo(UserStatus.SUSPENDED);
        setStatus(newStatus);
    }

    default void withdraw() {
        UserStatus newStatus = getStatus().transitionTo(UserStatus.WITHDRAWN);
        setStatus(newStatus);
    }
}
