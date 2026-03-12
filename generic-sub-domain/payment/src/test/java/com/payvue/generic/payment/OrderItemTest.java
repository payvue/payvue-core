package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.shared.Money;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemTest {

  @Test
  @DisplayName("OrderItem은 총 가격을 계산한다")
  void calculatesTotalPrice() {
    OrderItem item = new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 2, Money.of(1000L, "KRW"));

    assertThat(item.totalPrice().getAmount()).isEqualTo(new BigDecimal("2000.00"));
  }

  @Test
  @DisplayName("OrderItem은 빈 이름을 허용하지 않는다")
  void rejectsBlankName() {
    assertThatThrownBy(() -> new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), " ", 1, Money.of(1000L, "KRW")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("상품명이 비어");
  }

  @Test
  @DisplayName("OrderItem은 수량이 1 이상이어야 한다")
  void rejectsInvalidQuantity() {
    assertThatThrownBy(() -> new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 0, Money.of(1000L, "KRW")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("수량은 1 이상");
  }

  @Test
  @DisplayName("OrderItem은 단가가 0 이하일 수 없다")
  void rejectsNonPositivePrice() {
    assertThatThrownBy(() -> new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.zero("KRW")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("단가가 0 이하");
  }
}
