package com.payvue.core.transaction.command;

public interface TransactionPolicy {
  boolean shouldApply(TransactionCommand command, Transaction primaryResult);
}
