package com.payvue.supporting.interaction;

import java.util.ArrayList;
import java.util.List;

public final class ChainedEventHandler<I extends Intent> implements InteractionHandler<I> {

  private final List<InteractionHandler<I>> handlers;
  private final HandlerMatchingPolicy matchingPolicy;

  private ChainedEventHandler(
      List<InteractionHandler<I>> handlers,
      HandlerMatchingPolicy matchingPolicy
  ) {
    this.handlers = handlers;
    this.matchingPolicy = matchingPolicy;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder chain(InteractionHandler<?> handler) {
    return builder().add(handler);
  }

  @Override
  public boolean supports(Decision<?> decision) {
    if (decision == null) {
      return false;
    }
    for (InteractionHandler<I> handler : handlers) {
      if (handler.supports(decision)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void handle(InteractionContext context, Decision<? extends I> decision) {
    for (InteractionHandler<I> handler : handlers) {
      if (handler.supports(decision)) {
        handler.handle(context, decision);
        if(matchingPolicy == HandlerMatchingPolicy.FIRST_MATCH)
          return;
      }
    }
  }

  public static final class Builder {

    private final List<InteractionHandler<?>> handlers = new ArrayList<>();
    private HandlerMatchingPolicy matchingPolicy = HandlerMatchingPolicy.FIRST_MATCH;

    public Builder add(InteractionHandler<?> handler) {
      if (handler != null) {
        handlers.add(handler);
      }
      return this;
    }

    public Builder addAll(List<InteractionHandler<?>> handlers) {
      if (handlers != null) {
        for (InteractionHandler<?> handler : handlers) {
          add(handler);
        }
      }
      return this;
    }

    public Builder matchingPolicy(HandlerMatchingPolicy matchingPolicy) {
      if (matchingPolicy != null) {
        this.matchingPolicy = matchingPolicy;
      }
      return this;
    }

    public Builder firstMatch() {
      this.matchingPolicy = HandlerMatchingPolicy.FIRST_MATCH;
      return this;
    }

    public Builder allMatches() {
      this.matchingPolicy = HandlerMatchingPolicy.ALL_MATCHES;
      return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Intent> ChainedEventHandler<T> build() {
      List<InteractionHandler<T>> casted = new ArrayList<>();
      for (InteractionHandler<?> handler : handlers) {
        casted.add((InteractionHandler<T>) handler);
      }
      return new ChainedEventHandler<>(casted, matchingPolicy);
    }
  }
}
