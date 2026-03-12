package com.payvue.generic.payment;

import java.util.Objects;

final class PaymentExecutor<R extends PaymentProviderResponse> {

  private final PaymentCommandDispatcher<R> dispatcher;
  private final PaymentTranslator<R> translator;

  public PaymentExecutor(
      PaymentCommandDispatcher<R> dispatcher,
      PaymentTranslator<R> translator
  ) {
    this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher는 필수입니다");
    this.translator = Objects.requireNonNull(translator, "translator는 필수입니다");
  }

  public Payment execute(PaymentCommand command) {
    Objects.requireNonNull(command, "command는 필수입니다");
    R response = dispatcher.dispatch(command);
    Payment payment = translator.translate(response);
    return payment;
  }
}
