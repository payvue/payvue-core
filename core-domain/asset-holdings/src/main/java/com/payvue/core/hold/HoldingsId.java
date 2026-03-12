package com.payvue.core.hold;

import java.util.Objects;
import java.util.UUID;

public class HoldingsId {

    private final UUID value;

    private HoldingsId(UUID value) {
        this.value = Objects.requireNonNull(value, "HoldingsId는 null일 수 없습니다");
    }

    public static HoldingsId of(UUID value) {
        return new HoldingsId(value);
    }

    public static HoldingsId of(String value) {
        try {
            return new HoldingsId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 HoldingsId 형식: " + value, e);
        }
    }

    public static HoldingsId generate() {
        return new HoldingsId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoldingsId)) return false;
        HoldingsId that = (HoldingsId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
