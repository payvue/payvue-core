package com.payvue.core.transaction.command;

public interface TransactionBinding {
  BindingKey key();
  TransactionHandler handler();
}
