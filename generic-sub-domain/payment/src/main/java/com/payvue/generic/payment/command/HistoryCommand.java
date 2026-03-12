package com.payvue.generic.payment.command;

import com.payvue.generic.payment.PaymentCommand;

import java.util.Objects;

public record HistoryCommand(String startTime, String endTime) implements PaymentCommand {

  public HistoryCommand {
    Objects.requireNonNull(startTime, "startTime은 필수입니다");
    Objects.requireNonNull(endTime, "endTime은 필수입니다");
  }
}
