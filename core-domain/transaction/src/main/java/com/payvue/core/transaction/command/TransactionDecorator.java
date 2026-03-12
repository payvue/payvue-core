package com.payvue.core.transaction.command;

public interface TransactionDecorator {
  Transaction decorate(Transaction transaction);
}
