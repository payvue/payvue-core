package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.generic.payment.command.CancelCommand;
import com.payvue.generic.payment.command.ConfirmCommand;
import com.payvue.generic.payment.command.HistoryCommand;
import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.generic.payment.command.VirtualAccountReserveCommand;
import com.payvue.shared.Money;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentModeTest {

  @Test
  @DisplayName("SinglePaymentMode는 gateway 호출을 위임한다")
  void delegatesToGateway() {
    SinglePaymentMode<StubResponse> mode = new SinglePaymentMode<>();
    RecordingGateway gateway = new RecordingGateway();

    mode.reserve(gateway, new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW"))));
    UUID paymentId = UUID.randomUUID();
    mode.confirm(gateway, new ConfirmCommand(paymentId.toString()));
    mode.cancel(gateway, new CancelCommand("pid", "reason"));
    mode.history(gateway, new HistoryCommand("start", "end"));

    assertThat(gateway.reserveCalled).isTrue();
    assertThat(gateway.confirmedId).isEqualTo(paymentId);
    assertThat(gateway.canceledId).isEqualTo("pid");
    assertThat(gateway.historyStart).isEqualTo("start");
  }

  @Test
  @DisplayName("VirtualAccountPaymentMode는 가상계좌 reserve를 위임한다")
  void delegatesVirtualReserve() {
    VirtualAccountPaymentMode<StubResponse> mode = new VirtualAccountPaymentMode<>();
    RecordingVirtualGateway gateway = new RecordingVirtualGateway();

    mode.virtualReserve(
        gateway,
        new VirtualAccountReserveCommand(
            new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW")),
            "088"
        )
    );

    assertThat(gateway.virtualReserved).isTrue();
  }

  private static class RecordingGateway implements PaymentGateway<StubResponse> {
    private boolean reserveCalled;
    private UUID confirmedId;
    private String canceledId;
    private String historyStart;

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
      reserveCalled = true;
      return new StubResponse();
    }

    @Override
    public StubResponse confirm(String paymentId) {
      confirmedId = paymentId;
      return new StubResponse();
    }

    @Override
    public StubResponse cancel(String paymentId, String reason) {
      canceledId = paymentId;
      return new StubResponse();
    }

    @Override
    public StubResponse history(String startTime, String endTime) {
      historyStart = startTime;
      return new StubResponse();
    }
  }

  private static class StubResponse implements PaymentProviderResponse {
  }

  private static class RecordingVirtualGateway extends VirtualAccountCapableGateway<StubResponse> {
    private boolean virtualReserved;

    @Override
    public ProviderId providerId() {
      return new ProviderId("TOSS");
    }

    @Override
    public StubResponse reserveVirtual(OrderItem order, String bankCode) {
      virtualReserved = true;
      return new StubResponse();
    }

    @Override
    protected String defaultBankCode() {
      return "088";
    }
  }
}
