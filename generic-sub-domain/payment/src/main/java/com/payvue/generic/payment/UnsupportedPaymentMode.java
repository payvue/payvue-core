package com.payvue.generic.payment;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;

final class UnsupportedPaymentMode<R extends PaymentProviderResponse> implements PaymentMode<R> {

  private final PaymentModeType modeType;
  private final String message;

  public UnsupportedPaymentMode(PaymentModeType modeType, String message) {
    this.modeType = modeType;
    this.message = message;
  }

  @Override
  public PaymentModeType type() {
    return modeType;
  }

  @Override
  public R reserve(PaymentGateway<R> gateway, ReserveCommand command) {
    throw new UnsupportedOperationException(message);
  }

  @Override
  public R confirm(PaymentGateway<R> gateway, ConfirmCommand command) {
    throw new UnsupportedOperationException(message);
  }

  @Override
  public R cancel(PaymentGateway<R> gateway, CancelCommand command) {
    throw new UnsupportedOperationException(message);
  }

  @Override
  public R history(PaymentGateway<R> gateway, HistoryCommand command) {
    throw new UnsupportedOperationException(message);
  }
}
