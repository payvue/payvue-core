package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.shared.Money;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentSupportRegistryTest {

  @Test
  @DisplayName("등록된 결제 모듈은 providerId와 mode로 조회된다")
  void resolveReturnsRegisteredContext() {
    PaymentSupportRegistry registry = new PaymentSupportRegistry();
    ProviderId providerId = new ProviderId("NAVER");
    StubGateway gateway = new StubGateway(providerId);
    StubTranslator translator = new StubTranslator(providerId);

    registry.provider(providerId)
        .gateway(gateway)
        .translator(translator)
        .mode(PaymentModeType.SINGLE)
        .strategy(new SinglePaymentMode<>())
        .register();

    PaymentContext<?> context = registry.resolve(providerId, PaymentModeType.SINGLE);

    assertThat(context.providerId()).isEqualTo(providerId);
    assertThat(context.modeType()).isEqualTo(PaymentModeType.SINGLE);
    assertThat(gateway.lastOrder).isNull();

    Payment result = context.execute(new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 2, Money.of(1000L, "KRW"))));

    assertThat(gateway.lastOrder.name()).isEqualTo("coffee");
    assertThat(result.providerId()).isEqualTo(providerId);
    assertThat(result.status()).isEqualTo(PaymentStatus.PENDING);
  }

  @Test
  @DisplayName("지원하지 않는 provider/mode는 예외를 던진다")
  void resolveThrowsWhenMissing() {
    PaymentSupportRegistry registry = new PaymentSupportRegistry();

    assertThatThrownBy(() -> registry.resolve(new ProviderId("TOSS"), PaymentModeType.SINGLE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("지원하지 않는 결제 모듈입니다");
  }

  @Test
  @DisplayName("게이트웨이 제공자가 다르면 예외를 던진다")
  void registerThrowsWhenGatewayProviderMismatch() {
    PaymentSupportRegistry registry = new PaymentSupportRegistry();
    ProviderId providerId = new ProviderId("NAVER");

    assertThatThrownBy(() -> registry.provider(providerId)
        .gateway(new StubGateway(new ProviderId("OTHER")))
        .translator(new StubTranslator(providerId))
        .mode(PaymentModeType.SINGLE)
        .strategy(new SinglePaymentMode<>())
        .register())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("결제 게이트웨이 제공자가 일치하지 않습니다");
  }

  @Test
  @DisplayName("번역기 제공자가 다르면 예외를 던진다")
  void registerThrowsWhenTranslatorProviderMismatch() {
    PaymentSupportRegistry registry = new PaymentSupportRegistry();
    ProviderId providerId = new ProviderId("NAVER");

    assertThatThrownBy(() -> registry.provider(providerId)
        .gateway(new StubGateway(providerId))
        .translator(new StubTranslator(new ProviderId("OTHER")))
        .mode(PaymentModeType.SINGLE)
        .strategy(new SinglePaymentMode<>())
        .register())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("결제 번역기 제공자가 일치하지 않습니다");
  }

  private static class StubGateway implements PaymentGateway<StubResponse> {
    private final ProviderId providerId;
    private OrderItem lastOrder;

    private StubGateway(ProviderId providerId) {
      this.providerId = providerId;
    }

    @Override
    public ProviderId providerId() {
      return providerId;
    }

    @Override
    public boolean supports(PaymentModeType mode) {
      return mode == PaymentModeType.SINGLE;
    }

    @Override
    public StubResponse reserve(OrderItem order) {
      this.lastOrder = order;
      return new StubResponse();
    }

    @Override
    public StubResponse confirm(String paymentId) {
      return new StubResponse();
    }

    @Override
    public StubResponse cancel(String paymentId, String reason) {
      return new StubResponse();
    }

    @Override
    public StubResponse history(String startTime, String endTime) {
      return new StubResponse();
    }
  }

  private static class StubTranslator implements PaymentTranslator<StubResponse> {
    private final ProviderId providerId;

    private StubTranslator(ProviderId providerId) {
      this.providerId = providerId;
    }

    @Override
    public ProviderId providerId() {
      return providerId;
    }

    @Override
    public Payment translate(StubResponse response) {
      return Payment.of(PaymentStatus.PENDING, PaymentModeType.SINGLE, providerId, "external", Map.of());
    }
  }

  private static class StubResponse implements PaymentProviderResponse {
  }
}
