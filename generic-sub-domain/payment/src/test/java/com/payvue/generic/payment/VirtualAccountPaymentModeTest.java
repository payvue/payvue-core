package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.generic.payment.command.VirtualAccountReserveCommand;
import com.payvue.shared.Money;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VirtualAccountPaymentModeTest {

  @Test
  @DisplayName("가상계좌 모드는 일반 reserve를 지원하지 않는다")
  void reserveThrowsUnsupported() {
    VirtualAccountPaymentMode<StubResponse> mode = new VirtualAccountPaymentMode<>();

    assertThatThrownBy(() -> mode.reserve(new StubGateway(), new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW")))))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("가상계좌 모드는 지원하지 않는 게이트웨이면 예외를 던진다")
  void virtualReserveThrowsWhenGatewayUnsupported() {
    VirtualAccountPaymentMode<StubResponse> mode = new VirtualAccountPaymentMode<>();

    assertThatThrownBy(() -> mode.virtualReserve(new StubGateway(), new VirtualAccountReserveCommand(
        new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW")),
        "088"
    )))
    .isInstanceOf(IllegalArgumentException.class);
  }


  private static class StubGateway implements PaymentGateway<StubResponse> {
    @Override
    public ProviderId providerId() {
      return new ProviderId("TOSS");
    }

    @Override
    public boolean supports(PaymentModeType mode) {
      return false;
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
