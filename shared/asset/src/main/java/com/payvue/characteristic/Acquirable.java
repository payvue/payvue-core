package com.payvue.characteristic;

import java.util.Objects;

public non-sealed class Acquirable implements AssetCharacteristic {

    private AcquisitionMethod method;

    private Acquirable(AcquisitionMethod method) {
        this.method = Objects.requireNonNull(method);
    }

    public static Acquirable purchased() {
        return new Acquirable(AcquisitionMethod.PURCHASED);
    }

    public static Acquirable received() {
        return new Acquirable(AcquisitionMethod.RECEIVED);
    }

    public static Acquirable shared() {
        return new Acquirable(AcquisitionMethod.SHARED);
    }

    public static Acquirable contributed() {
        return new Acquirable(AcquisitionMethod.CONTRIBUTED);
    }

    public AcquisitionMethod method() {
        return method;
    }

    public boolean isPurchased() {
        return method == AcquisitionMethod.PURCHASED;
    }

    public boolean isReceived() {
        return method == AcquisitionMethod.RECEIVED;
    }

    public boolean isShared() {
        return method == AcquisitionMethod.SHARED;
    }

    public boolean isContributed() {
        return method == AcquisitionMethod.CONTRIBUTED;
    }

    public void setMethod(AcquisitionMethod method) {
        this.method = Objects.requireNonNull(method);
    }

    public enum AcquisitionMethod {
        PURCHASED, RECEIVED, SHARED, CONTRIBUTED
    }
}
