package com.payvue.generic.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.generic.payment.command.ReserveCommand;
import com.payvue.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentCommandDispatcherTest {

  @Test
  @DisplayName("등록된 커맨드는 디스패처를 통해 실행된다")
  void dispatchesRegisteredCommand() {
    PaymentCommandDispatcher<StubResponse> dispatcher = PaymentCommandDispatcher.<StubResponse>builder()
        .on(ReserveCommand.class, cmd -> new StubResponse("reserve:" + cmd.order().name()))
        .build();

    StubResponse response = dispatcher.dispatch(new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW"))));

    assertThat(response.message()).isEqualTo("reserve:coffee");
  }

  @Test
  @DisplayName("등록되지 않은 커맨드는 예외를 던진다")
  void throwsWhenMissingHandler() {
    PaymentCommandDispatcher<StubResponse> dispatcher = PaymentCommandDispatcher.<StubResponse>builder().build();

    assertThatThrownBy(() -> dispatcher.dispatch(new ReserveCommand(new OrderItem(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"), "coffee", 1, Money.of(1000L, "KRW")))))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("지원하지 않는 결제 커맨드");
  }

  private record StubResponse(String message) implements PaymentProviderResponse {
  }
}
