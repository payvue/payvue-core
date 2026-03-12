package com.payvue.generic.payment;

import com.payvue.shared.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record OrderItem(UUID orderId, String name, int quantity, Money unitPrice) {

  public OrderItem {
    Objects.requireNonNull(orderId, "orderId는 필수입니다");
    Objects.requireNonNull(name, "name는 필수입니다");
    Objects.requireNonNull(unitPrice, "unitPrice는 필수입니다");
    if (name.isBlank()) {
      throw new IllegalArgumentException("상품명이 비어 있습니다.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
    }
    if (unitPrice.isNegative() || unitPrice.isZero()) {
      throw new IllegalArgumentException("단가가 0 이하일 수 없습니다.");
    }
  }

  public Money totalPrice() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
}
