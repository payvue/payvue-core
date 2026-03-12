package com.payvue.generic.payment;

import com.payvue.generic.payment.command.*;

final class VirtualAccountPaymentMode<R extends PaymentProviderResponse> implements PaymentMode<R> {

  @Override
  public PaymentModeType type() {
    return PaymentModeType.VIRTUAL_ACCOUNT;
  }

  @Override
  public R reserve(PaymentGateway<R> gateway, ReserveCommand command) {
    throw new UnsupportedOperationException("가상계좌 모드에서는 일반 reserve를 지원하지 않습니다.");
  }

  public R virtualReserve(PaymentGateway<R> gateway, VirtualAccountReserveCommand command) {
    if (gateway instanceof VirtualAccountCapableGateway<?> vg) {
      return ((VirtualAccountCapableGateway<R>) vg).reserveVirtual(command.order(), command.bankCode());
    }
    throw new IllegalArgumentException("가상계좌를 지원하지 않는 게이트웨이입니다.");
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
