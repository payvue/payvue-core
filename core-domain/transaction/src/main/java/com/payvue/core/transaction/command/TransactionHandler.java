package com.payvue.core.transaction.command;

public interface TransactionHandler {
  Transaction handle(TransactionCommand command);
}
