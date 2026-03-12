package com.payvue.generic.payment;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;

final class SinglePaymentMode<R extends PaymentProviderResponse> implements PaymentMode<R> {

  @Override
  public PaymentModeType type() {
    return PaymentModeType.SINGLE;
  }

  @Override
  public R reserve(PaymentGateway<R> gateway, ReserveCommand command) {
    return gateway.reserve(command.order());
  }

  @Override
  public R confirm(PaymentGateway<R> gateway, ConfirmCommand command) {
    return gateway.confirm(command.paymentId());
  }

  @Override
  public R cancel(PaymentGateway<R> gateway, CancelCommand command) {
    return gateway.cancel(command.paymentId(), command.reason());
  }

  @Override
  public R history(PaymentGateway<R> gateway, HistoryCommand command) {
    return gateway.history(command.startTime(), command.endTime());
  }
}
