package com.payvue.core.relation;

import lombok.Getter;

@Getter
public enum ConnectionStatus implements ConnectionState {

    PENDING("대기중") {
        @Override
        public ConnectionStatus activate() {
            return ACTIVE;
        }

        @Override
        public ConnectionStatus terminate() {
            return TERMINATED;
        }
    },

    ACTIVE("활성") {
        @Override
        public ConnectionStatus activate() {
            return ACTIVE;
        }

        @Override
        public ConnectionStatus suspend() {
            return SUSPENDED;
        }

        @Override
        public ConnectionStatus terminate() {
            return TERMINATED;
        }

        @Override
        public boolean isActive() {
            return true;
        }
    },

    SUSPENDED("정지") {
        @Override
        public ConnectionStatus activate() {
            return ACTIVE;
        }

        @Override
        public ConnectionStatus terminate() {
            return TERMINATED;
        }
    },

    TERMINATED("종료");

    private final String description;

    ConnectionStatus(String description) {
        this.description = description;
    }
}
