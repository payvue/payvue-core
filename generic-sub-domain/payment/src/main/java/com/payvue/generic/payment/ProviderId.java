package com.payvue.generic.payment;

import java.util.Objects;

public record ProviderId(String value) {

  public ProviderId {
    Objects.requireNonNull(value, "value는 필수입니다");
    if (value.isBlank()) {
      throw new IllegalArgumentException("value는 비어 있을 수 없습니다.");
    }
  }
}
