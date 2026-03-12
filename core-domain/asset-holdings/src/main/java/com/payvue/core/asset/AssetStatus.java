package com.payvue.core.asset;

public enum AssetStatus {

    ACTIVE("활성") {
        @Override
        public AssetStatus use() {
            return USED;
        }

        @Override
        public AssetStatus expire() {
            return EXPIRED;
        }

        @Override
        public AssetStatus suspend() {
            return SUSPENDED;
        }
    },

    USED("사용 완료") {
        @Override
        public AssetStatus use() {
            throw new IllegalStateException("이미 사용 완료된 자산입니다");
        }

        @Override
        public AssetStatus expire() {
            return this; // 이미 종료 상태
        }

        @Override
        public AssetStatus suspend() {
            throw new IllegalStateException("사용 완료된 자산은 정지할 수 없습니다");
        }
    },

    EXPIRED("만료됨") {
        @Override
        public AssetStatus use() {
            throw new IllegalStateException("만료된 자산은 사용할 수 없습니다");
        }

        @Override
        public AssetStatus expire() {
            return this; // 이미 만료 상태
        }

        @Override
        public AssetStatus suspend() {
            throw new IllegalStateException("만료된 자산은 정지할 수 없습니다");
        }
    },

    SUSPENDED("일시 정지") {
        @Override
        public AssetStatus use() {
            throw new IllegalStateException("정지된 자산은 사용할 수 없습니다");
        }

        @Override
        public AssetStatus expire() {
            return EXPIRED;
        }

        @Override
        public AssetStatus suspend() {
            return this; // 이미 정지 상태
        }

        @Override
        public AssetStatus activate() {
            return ACTIVE;
        }
    };

    private final String description;

    AssetStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract AssetStatus use();
    public abstract AssetStatus expire();
    public abstract AssetStatus suspend();

    public AssetStatus activate() {
        throw new IllegalStateException("현재 상태에서는 활성화할 수 없습니다: " + this);
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isUsed() {
        return this == USED;
    }

    public boolean isExpired() {
        return this == EXPIRED;
    }

    public boolean isSuspended() {
        return this == SUSPENDED;
    }
}
