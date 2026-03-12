package com.payvue.core.asset.characteristic;

public final class CapabilityResult {
  private final boolean success;
  private final String message;
  private final Object payload;

  private CapabilityResult(boolean success, String message, Object payload) {
    this.success = success;
    this.message = message;
    this.payload = payload;
  }

  public static CapabilityResult success(Object payload) {
    return new CapabilityResult(true, "ok", payload);
  }

  public static CapabilityResult success(String message, Object payload) {
    return new CapabilityResult(true, message, payload);
  }

  public static CapabilityResult failure(String message) {
    return new CapabilityResult(false, message, null);
  }

  public boolean isSuccess() {
    return success;
  }

  public boolean isFailure() {
    return !success;
  }

  public String getMessage() {
    return message;
  }

  public Object getPayload() {
    return payload;
  }
}
