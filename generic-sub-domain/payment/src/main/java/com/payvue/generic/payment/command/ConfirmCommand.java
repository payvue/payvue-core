package com.payvue.generic.payment.command;

import com.payvue.generic.payment.PaymentCommand;

import java.util.Objects;
public record ConfirmCommand(String paymentId) implements PaymentCommand {

  public ConfirmCommand {
    Objects.requireNonNull(paymentId, "paymentId는 필수입니다");
  }
}
