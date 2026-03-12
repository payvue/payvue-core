package com.payvue.generic.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final class PaymentCommandDispatcher<R extends PaymentProviderResponse> {

  private final Map<Class<? extends PaymentCommand>, Function<PaymentCommand, R>> handlers;

  private PaymentCommandDispatcher(Map<Class<? extends PaymentCommand>, Function<PaymentCommand, R>> handlers) {
    this.handlers = handlers;
  }

  public R dispatch(PaymentCommand command) {
    Function<PaymentCommand, R> fn = handlers.get(command.getClass());
    if (fn == null) {
      throw new UnsupportedOperationException("지원하지 않는 결제 커맨드입니다: " + command.getClass().getSimpleName());
    }
    return fn.apply(command);
  }

  public static <R extends PaymentProviderResponse> Builder<R> builder() {
    return new Builder<>();
  }

  static final class Builder<R extends PaymentProviderResponse> {
    private final Map<Class<? extends PaymentCommand>, Function<PaymentCommand, R>> handlers = new HashMap<>();

    public <C extends PaymentCommand> Builder<R> on(Class<C> type, Function<C, R> fn) {
      handlers.put(type, cmd -> fn.apply(type.cast(cmd)));
      return this;
    }

    public PaymentCommandDispatcher<R> build() {
      return new PaymentCommandDispatcher<>(handlers);
    }
  }
}
