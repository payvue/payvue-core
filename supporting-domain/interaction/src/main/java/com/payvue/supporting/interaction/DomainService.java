package com.payvue.supporting.interaction;

public interface DomainService<I extends Intent> {

  boolean supports(Decision<?> decision);

  default Interaction execute(InteractionContext context, Decision<I> decision) {
    return execute(decision);
  }

  Interaction execute(Decision<I> decision);

  default Interaction execute(
      InteractionContext context,
      Decision<I> decision,
      InteractionHandler<I> handler
  ) {
    return execute(context, decision);
  }
}
