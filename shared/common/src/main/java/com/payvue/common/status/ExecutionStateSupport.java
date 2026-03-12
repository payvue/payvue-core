package com.payvue.common.status;


public interface ExecutionStateSupport {

    ExecutionState getStatus();
    void setStatus(ExecutionState newStatus);
    void setMessage(String message);
    default boolean isPendingApproval() {
        return getStatus().isPendingApproval();
    }

    default boolean isApproved() {
        return getStatus().isApproved();
    }

    default boolean isRejected() {
        return getStatus().isRejected();
    }

    default boolean isCompleted() {
        return getStatus().isCompleted();
    }

    default boolean isFailed() {
        return getStatus().isFailed();
    }

    default boolean canExecute() {
        return getStatus().canExecute();
    }

    default boolean isFinal() {
        return getStatus().isFinal();
    }

    default boolean canTransitionTo(ExecutionState newStatus) {
        return getStatus().canTransitionTo(newStatus);
    }

    default void approve() {
        ExecutionState newStatus = getStatus().transitionTo(ExecutionState.APPROVED);
        setStatus(newStatus);
    }

    default void reject(String reason) {
        ExecutionState newStatus = getStatus().transitionTo(ExecutionState.REJECTED);
        setStatus(newStatus);
        setMessage(reason);
    }

    default void complete() {
        ExecutionState newStatus = getStatus().transitionTo(ExecutionState.COMPLETED);
        setStatus(newStatus);
    }

    default void fail() {
        ExecutionState newStatus = getStatus().transitionTo(ExecutionState.FAILED);
        setStatus(newStatus);
    }
}
