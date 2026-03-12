package com.payvue.characteristic;

import java.util.Objects;

/**
 * Ownable - 소유 가능 능력 (Mutable Class)
 */
public non-sealed class Ownable implements AssetCharacteristic {

    private OwnershipType type;

    private Ownable(OwnershipType type) {
        this.type = Objects.requireNonNull(type);
    }

    public static Ownable personal() {
        return new Ownable(OwnershipType.PERSONAL);
    }

    public static Ownable shared() {
        return new Ownable(OwnershipType.SHARED);
    }

    public OwnershipType type() {
        return type;
    }

    public boolean isPersonal() {
        return type == OwnershipType.PERSONAL;
    }

    public boolean isShared() {
        return type == OwnershipType.SHARED;
    }

    public void setType(OwnershipType type) {
        this.type = Objects.requireNonNull(type);
    }

    public enum OwnershipType {
        PERSONAL, SHARED
    }
}
