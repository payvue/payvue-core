package com.payvue.core.transaction.command;

import java.util.List;
import java.util.Objects;

final class PolicyAwareHandlerChain implements TransactionHandler {

  private final TransactionPolicy policy;
  private final TransactionHandler defaults;
  private final List<TransactionDecorator> decorators;

  public PolicyAwareHandlerChain(
          TransactionPolicy policy, TransactionHandler defaults,
          List<TransactionDecorator> decorators
  ) {
    this.policy = Objects.requireNonNull(policy, "policy는 필수입니다.");
    this.defaults = Objects.requireNonNull(defaults, "defaults는 필수입니다.");
    this.decorators = List.copyOf(Objects.requireNonNull(decorators, "decorators는 필수입니다."));
  }

  @Override
  public Transaction handle(TransactionCommand command) {

    Transaction transaction = defaults.handle(command);

    if (policy.shouldApply(command, transaction)) {
      for (TransactionDecorator decorator : decorators) {
        transaction = decorator.decorate(transaction);
      }
    }

    return transaction;
  }

}
