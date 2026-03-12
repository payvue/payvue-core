package com.payvue.core.transaction.command;

import java.util.Objects;

record BindingKey(String assetType, TransactionType transactionType) {

  BindingKey {
    Objects.requireNonNull(assetType, "assetType은 필수입니다.");
    Objects.requireNonNull(transactionType, "transactionType은 필수입니다.");
  }
}
