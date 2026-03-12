package com.payvue.core.asset;

public class NotCapabilityException extends RuntimeException {

    public NotCapabilityException() {
        super("Required capability is not present.");
    }
}
