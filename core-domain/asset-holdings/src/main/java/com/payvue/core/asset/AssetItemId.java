package com.payvue.core.asset;

import java.util.Objects;
import java.util.UUID;

public class AssetItemId {

    private final UUID value;

    private AssetItemId(UUID value) {
        this.value = Objects.requireNonNull(value, "AssetItemId는 null일 수 없습니다");
    }

    public static AssetItemId of(UUID value) {
        return new AssetItemId(value);
    }

    public static AssetItemId of(String value) {
        try {
            return new AssetItemId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 AssetItemId 형식: " + value, e);
        }
    }

    public static AssetItemId generate() {
        return new AssetItemId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetItemId)) return false;
        AssetItemId that = (AssetItemId) o;
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
