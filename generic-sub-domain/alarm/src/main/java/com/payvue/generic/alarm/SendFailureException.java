package com.payvue.generic.alarm;

public final class SendFailureException extends Exception {
    public SendFailureException(String message) {
        super(message);
    }

    public SendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
