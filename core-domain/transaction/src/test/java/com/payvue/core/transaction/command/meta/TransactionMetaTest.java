package com.payvue.core.transaction.command.meta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TransactionMeta")
class TransactionMetaTest {

  @Test
  @DisplayName("fromRawMap은 문자열 키를 열거형으로 변환한다")
  void fromRawMapParsesEnumKeys() {
    TransactionMeta meta = TransactionMeta.fromRawMap(Map.of(
        TransactionMetaKey.MERCHANT_ID.name(), "merchant-1",
        TransactionMetaKey.PAYMENT_METHOD_ID.name(), "pm-1"
    ));

    assertThat(meta.get(TransactionMetaKey.MERCHANT_ID)).isEqualTo("merchant-1");
    assertThat(meta.get(TransactionMetaKey.PAYMENT_METHOD_ID)).isEqualTo("pm-1");
  }

  @Test
  @DisplayName("merge는 후속 메타의 값을 우선 적용한다")
  void mergeUsesFollowUpValues() {
    TransactionMeta base = TransactionMeta.of(Map.of(TransactionMetaKey.MERCHANT_ID, "m-1"));
    TransactionMeta followUp = TransactionMeta.of(Map.of(TransactionMetaKey.MERCHANT_ID, "m-2"));

    TransactionMeta merged = base.merge(followUp);

    assertThat(merged.get(TransactionMetaKey.MERCHANT_ID)).isEqualTo("m-2");
  }
}
