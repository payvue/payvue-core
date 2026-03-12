package com.payvue.generic.payment.command;

import com.payvue.generic.payment.OrderItem;
import com.payvue.generic.payment.PaymentCommand;

import java.util.Objects;

public record ReserveCommand(OrderItem order) implements PaymentCommand {

  public ReserveCommand {
    Objects.requireNonNull(order, "order는 필수입니다");
  }
}
