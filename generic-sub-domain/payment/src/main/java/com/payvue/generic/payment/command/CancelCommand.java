package com.payvue.generic.payment.command;

import com.payvue.generic.payment.PaymentCommand;

import java.util.Objects;

public record CancelCommand(String paymentId, String reason) implements PaymentCommand {

  public CancelCommand {
    Objects.requireNonNull(paymentId, "paymentId는 필수입니다");
    Objects.requireNonNull(reason, "reason은 필수입니다");
  }
}
