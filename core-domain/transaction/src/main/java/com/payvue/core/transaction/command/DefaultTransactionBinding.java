package com.payvue.core.transaction.command;

final class DefaultTransactionBinding implements TransactionBinding {

  private final BindingKey key;
  private final TransactionHandler handler;

  public DefaultTransactionBinding(BindingKey key, TransactionHandler handler) {
    this.key = key;
    this.handler = handler;
  }

  @Override
  public BindingKey key() {
    return key;
  }

  @Override
  public TransactionHandler handler() {
    return handler;
  }
}
