package com.payvue.generic.payment;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.generic.payment.command.VirtualAccountReserveCommand;

import java.util.Objects;

public final class PaymentContext<R extends PaymentProviderResponse> {

  private final ProviderId providerId;
  private final PaymentModeType modeType;
  private final PaymentMode<R> mode;
  private final PaymentExecutor<R> executor;
  private final PaymentCommandDispatcher<R> dispatcher;

  public PaymentContext(
      ProviderId providerId,
      PaymentModeType modeType,
      PaymentMode<R> mode,
      PaymentGateway<R> gateway,
      PaymentTranslator<R> translator,
      PaymentCommandDispatcher<R> dispatcher
  ) {
    this.providerId = Objects.requireNonNull(providerId, "providerId는 필수입니다");
    this.modeType = Objects.requireNonNull(modeType, "modeType은 필수입니다");
    this.mode = Objects.requireNonNull(mode, "mode는 필수입니다");
    this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher는 필수입니다");
    this.executor = new PaymentExecutor<>(this.dispatcher, translator);
    validateModeType();
  }

  public ProviderId providerId() {
    return providerId;
  }

  public PaymentModeType modeType() {
    return modeType;
  }

  public Payment execute(PaymentCommand command) {
    return executor.execute(command);
  }

  private void validateModeType() {
    if (mode.type() != modeType) {
      throw new IllegalArgumentException("결제 모드 타입이 일치하지 않습니다.");
    }
  }
}
