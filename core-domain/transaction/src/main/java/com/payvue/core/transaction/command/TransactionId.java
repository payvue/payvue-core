package com.payvue.core.transaction.command;

import java.util.UUID;

public record TransactionId(String value) {

  public TransactionId {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("transactionId는 필수입니다.");
    }
  }

  public static TransactionId generate() {
    return new TransactionId(UUID.randomUUID().toString());
  }
}
