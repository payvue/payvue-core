package com.payvue.core.transaction.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.common.status.ExecutionState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TransactionBindingRegistry")
class TransactionBindingRegistryTest {

  private static final String ASSET_TYPE = "CASH";

  @Test
  @DisplayName("바인딩된 키로 핸들러를 조회한다")
  void resolvesBoundHandler() {
    TransactionHandler handler = command -> Transaction.reconstitute(
        TransactionId.generate(),
        command.type(),
        command.assetType(),
        command.from(),
        command.to(),
        command.item(),
        command.amount().toLong(),
        command.actor().actorId(),
        command.item().getName(),
        command.item().getCategory().getValue(),
        ExecutionState.COMPLETED,
        java.time.Instant.now(),
        command.meta()
    );
    TransactionBindingRegistry registry = TransactionBindingRegistry.builder()
        .bind(new BindingKey(ASSET_TYPE, TransactionType.PAYMENT), handler)
        .build();

    TransactionBinding binding = registry.resolve(ASSET_TYPE, TransactionType.PAYMENT);

    assertThat(binding.key().assetType()).isEqualTo(ASSET_TYPE);
    assertThat(binding.key().transactionType()).isEqualTo(TransactionType.PAYMENT);
    assertThat(binding.handler()).isSameAs(handler);
  }

  @Test
  @DisplayName("중복 바인딩이 등록되면 예외가 발생한다")
  void rejectsDuplicateBinding() {
    TransactionHandler handler = command -> Transaction.reconstitute(
        TransactionId.generate(),
        command.type(),
        command.assetType(),
        command.from(),
        command.to(),
        command.item(),
        command.amount().toLong(),
        command.actor().actorId(),
        command.item().getName(),
        command.item().getCategory().getValue(),
        ExecutionState.COMPLETED,
        java.time.Instant.now(),
        command.meta()
    );
    TransactionBindingRegistry.Builder builder = TransactionBindingRegistry.builder()
        .bind(new BindingKey(ASSET_TYPE, TransactionType.PAYMENT), handler);

    assertThatThrownBy(() -> builder.bind(
        new BindingKey(ASSET_TYPE, TransactionType.PAYMENT), handler
    )).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록되지 않은 조합은 예외를 던진다")
  void throwsWhenBindingMissing() {
    TransactionBindingRegistry registry = TransactionBindingRegistry.builder().build();

    assertThatThrownBy(() -> registry.resolve("UNKNOWN", TransactionType.PAYMENT))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
