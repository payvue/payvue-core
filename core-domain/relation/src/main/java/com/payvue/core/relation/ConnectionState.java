package com.payvue.core.relation;

interface ConnectionState {

    default ConnectionStatus activate() {
        throw invalidTransition("activate");
    }

    default ConnectionStatus suspend() {
        throw invalidTransition("suspend");
    }

    default ConnectionStatus terminate() {
        throw invalidTransition("terminate");
    }

    default boolean isActive() {
        return false;
    }

    private IllegalStateException invalidTransition(String action) {
        return new IllegalStateException(
                String.format("상태에서는 %s 할 수 없습니다.", action)
        );
    }
}
