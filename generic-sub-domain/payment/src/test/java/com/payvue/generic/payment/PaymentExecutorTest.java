package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentExecutorTest {

  @Test
  @DisplayName("PaymentExecutor는 디스패처와 번역기를 순서대로 호출한다")
  void executeDispatchesAndTranslates() {
    java.util.concurrent.atomic.AtomicBoolean dispatched = new java.util.concurrent.atomic.AtomicBoolean(false);
    PaymentCommandDispatcher<StubResponse> dispatcher = PaymentCommandDispatcher.<StubResponse>builder()
        .on(ReserveCommand.class, cmd -> {
          dispatched.set(true);
          return new StubResponse();
        })
        .build();
    RecordingTranslator translator = new RecordingTranslator();
    PaymentExecutor<StubResponse> executor = new PaymentExecutor<>(dispatcher, translator);

    Payment payment = executor.execute(new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW"))));

    assertThat(dispatched.get()).isTrue();
    assertThat(translator.translated).isTrue();
    assertThat(payment.status()).isEqualTo(PaymentStatus.PENDING);
  }

  private static class RecordingTranslator implements PaymentTranslator<StubResponse> {
    private boolean translated;

    @Override
    public ProviderId providerId() {
      return new ProviderId("NAVER");
    }

    @Override
    public Payment translate(StubResponse response) {
      translated = true;
      return Payment.of(PaymentStatus.PENDING, PaymentModeType.SINGLE, providerId(), "external", java.util.Map.of());
    }
  }

  private static class StubResponse implements PaymentProviderResponse {
  }
}
