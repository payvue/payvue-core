package com.payvue.generic.payment;

import java.util.HashMap;
import java.util.Map;

public final class PaymentSupportRegistry {

  private final Map<Key, PaymentContext<?>> modules = new HashMap<>();

  public <R extends PaymentProviderResponse> ProviderBuilder<R> provider(
      ProviderId providerId
  ) {
    return new ProviderBuilder<>(this, providerId);
  }

  void register(PaymentContext<?> module, PaymentModeType modeType) {
    modules.put(new Key(module.providerId(), modeType), module);
  }

  public PaymentContext<?> resolve(ProviderId providerId, PaymentModeType modeType) {
    PaymentContext<?> module = modules.get(new Key(providerId, modeType));
    if (module == null) {
      throw new IllegalArgumentException(
          "지원하지 않는 결제 모듈입니다: " + providerId + "/" + modeType
      );
    }
    return module;
  }

  public static final class ProviderBuilder<R extends PaymentProviderResponse> {

    private final PaymentSupportRegistry registry;
    private final ProviderId providerId;
    public PaymentCommandDispatcher<R> dispatcher;
    private PaymentGateway<R> gateway;
    private PaymentTranslator<R> translator;

    ProviderBuilder(PaymentSupportRegistry registry, ProviderId providerId) {
      this.registry = registry;
      this.providerId = providerId;
    }

    public ProviderBuilder<R> gateway(PaymentGateway<R> gateway) {
      this.gateway = gateway;
      return this;
    }

    public ProviderBuilder<R> translator(PaymentTranslator<R> translator) {
      this.translator = translator;
      return this;
    }

    public ProviderBuilder<R> dispatcher(PaymentCommandDispatcher<R> dispatcher) {
      this.dispatcher = dispatcher;
      return this;
    }

    public ProviderBuilder<R> modes(java.util.function.Consumer<ModeRegistrar<R>> consumer) {
      if (consumer != null) {
        consumer.accept(new ModeRegistrar<>(this));
      }
      return this;
    }

    public ModeBuilder<R> mode(PaymentModeType modeType) {
      return new ModeBuilder<>(this, modeType);
    }
  }

  public static final class ModeRegistrar<R extends PaymentProviderResponse> {

    private final ProviderBuilder<R> providerBuilder;

    ModeRegistrar(ProviderBuilder<R> providerBuilder) {
      this.providerBuilder = providerBuilder;
    }

    public ModeRegistrar<R> single(
        java.util.function.Consumer<ModeBuilder<R>> consumer
    ) {
      return register(PaymentModeType.SINGLE, consumer);
    }

    public ModeRegistrar<R> recurring(
        java.util.function.Consumer<ModeBuilder<R>> consumer
    ) {
      return register(PaymentModeType.RECURRING, consumer);
    }

    public ModeRegistrar<R> batch(
        java.util.function.Consumer<ModeBuilder<R>> consumer
    ) {
      return register(PaymentModeType.BATCH, consumer);
    }

    public ModeRegistrar<R> register(
        PaymentModeType modeType,
        java.util.function.Consumer<ModeBuilder<R>> consumer
    ) {
      if (consumer != null) {
        ModeBuilder<R> builder = providerBuilder.mode(modeType);
        consumer.accept(builder);
        builder.register();
      }
      return this;
    }

  }

  public static final class ModeBuilder<R extends PaymentProviderResponse> {

    private final ProviderBuilder<R> parent;
    private final PaymentModeType modeType;
    private PaymentMode<R> mode;
    private PaymentGateway<R> gateway;
    private PaymentTranslator<R> translator;
    private PaymentCommandDispatcher<R> dispatcher;

    ModeBuilder(ProviderBuilder<R> parent, PaymentModeType modeType) {
      this.parent = parent;
      this.modeType = modeType;
    }

    public ModeBuilder<R> strategy(PaymentMode<R> mode) {
      this.mode = mode;
      return this;
    }

    public ModeBuilder<R> gateway(PaymentGateway<R> gateway) {
      this.gateway = gateway;
      return this;
    }

    public ModeBuilder<R> translator(PaymentTranslator<R> translator) {
      this.translator = translator;
      return this;
    }

    public ModeBuilder<R> dispatcher(PaymentCommandDispatcher<R> dispatcher) {
      this.dispatcher = dispatcher;
      return this;
    }

    public PaymentContext<R> register() {
      PaymentMode<R> resolvedMode = mode;
      PaymentGateway<R> resolvedGateway = gateway != null ? gateway : parent.gateway;
      PaymentTranslator<R> resolvedTranslator = translator != null ? translator : parent.translator;
      PaymentCommandDispatcher<R> resolvedDispatcher = dispatcher != null ? dispatcher : parent.dispatcher;
      if (!resolvedGateway.providerId().equals(parent.providerId)) {
        throw new IllegalArgumentException("결제 게이트웨이 제공자가 일치하지 않습니다.");
      }
      if (!resolvedTranslator.providerId().equals(parent.providerId)) {
        throw new IllegalArgumentException("결제 번역기 제공자가 일치하지 않습니다.");
      }
      PaymentContext<R> module = new PaymentContext(
          parent.providerId,
          modeType,
          resolvedMode,
          resolvedGateway,
          resolvedTranslator,
          resolvedDispatcher
      );
      parent.registry.register(module, modeType);
      return module;
    }
  }

  private record Key(ProviderId providerId, PaymentModeType modeType) {}
}
