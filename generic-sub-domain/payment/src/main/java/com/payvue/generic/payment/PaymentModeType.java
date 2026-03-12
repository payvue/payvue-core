package com.payvue.generic.payment;

public enum PaymentModeType {
  SINGLE,
  BATCH,
  RECURRING,
  VIRTUAL_ACCOUNT;

  public static PaymentModeType from(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new IllegalArgumentException("payment provider is required");
    }
    return valueOf(raw.trim().toUpperCase());
  }
}
