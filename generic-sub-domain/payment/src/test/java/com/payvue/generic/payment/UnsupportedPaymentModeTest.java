package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.shared.Money;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UnsupportedPaymentModeTest {

  @Test
  @DisplayName("지원하지 않는 모드는 모든 커맨드에서 예외를 던진다")
  void throwsForAllCommands() {
    UnsupportedPaymentMode<StubResponse> mode = new UnsupportedPaymentMode<>(
        PaymentModeType.RECURRING,
        "not supported"
    );
    PaymentGateway<StubResponse> gateway = new StubGateway();

    assertThatThrownBy(() -> mode.reserve(gateway, new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW")))))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("not supported");
    assertThatThrownBy(() -> mode.confirm(gateway, new ConfirmCommand(java.util.UUID.randomUUID().toString())))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("not supported");
    assertThatThrownBy(() -> mode.cancel(gateway, new CancelCommand("pid", "reason")))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("not supported");
    assertThatThrownBy(() -> mode.history(gateway, new HistoryCommand("start", "end")))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("not supported");
  }

  private static class StubGateway implements PaymentGateway<StubResponse> {
    @Override
    public ProviderId providerId() {
      return new ProviderId("stub");
    }

    @Override
    public boolean supports(PaymentModeType mode) {
      return true;
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

  private static class StubResponse implements PaymentProviderResponse {
  }
}
