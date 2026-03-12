package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.generic.payment.command.VirtualAccountReserveCommand;
import com.payvue.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentContextTest {

  @Test
  @DisplayName("PaymentContext는 모드 타입이 다르면 예외를 던진다")
  void throwsWhenModeTypeMismatch() {
    assertThatThrownBy(() -> new PaymentContext<>(
        new ProviderId("NAVER"),
        PaymentModeType.SINGLE,
        new VirtualAccountPaymentMode<>(),
        new StubGateway(),
        new StubTranslator(),
        null
    ))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("결제 모드 타입이 일치하지 않습니다");
  }

  private static class StubGateway implements PaymentGateway<StubResponse> {
    @Override
    public ProviderId providerId() {
      return new ProviderId("NAVER");
    }

    @Override
    public boolean supports(PaymentModeType mode) {
      return mode == PaymentModeType.SINGLE;
    }

    @Override
    public StubResponse reserve(OrderItem order) {
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

    private StubTranslator() {
      this(new ProviderId("TOSS"));
    }

    private StubTranslator(ProviderId providerId) {
      this.providerId = providerId;
    }

    @Override
    public ProviderId providerId() {
      return providerId;
    }

    @Override
    public Payment translate(StubResponse response) {
      return Payment.of(PaymentStatus.PENDING, PaymentModeType.VIRTUAL_ACCOUNT, providerId, "external", java.util.Map.of());
    }
  }

  private static class StubResponse implements PaymentProviderResponse {
  }
}
