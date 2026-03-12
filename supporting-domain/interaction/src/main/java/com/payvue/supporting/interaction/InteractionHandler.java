package com.payvue.supporting.interaction;

public interface InteractionHandler<I extends Intent> {

  boolean supports(Decision<?> decision);

  void handle(InteractionContext context, Decision<? extends I> decision);
}
