package com.payvue.generic.payment;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;

public interface PaymentMode<R extends PaymentProviderResponse> {

  PaymentModeType type();

  R reserve(PaymentGateway<R> gateway, ReserveCommand command);

  R confirm(PaymentGateway<R> gateway, ConfirmCommand command);

  R cancel(PaymentGateway<R> gateway, CancelCommand command);

  R history(PaymentGateway<R> gateway, HistoryCommand command);
}
