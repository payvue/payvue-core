package com.payvue.supporting.interaction;

public final class InteractionFlow {

  private final String name;
  private final DomainService<?> domainService;
  private final InteractionHandler<?> handler;

  InteractionFlow(String name, DomainService<?> domainService, InteractionHandler<?> handler) {
    this.name = name;
    this.domainService = domainService;
    this.handler = handler;
  }

  public String name() {
    return name;
  }

  public boolean supports(Decision<?> decision) {
    return domainService.supports(decision);
  }

  public <I extends Intent> Interaction execute(InteractionContext context, Decision<I> decision) {
    Interaction interaction = Interaction.pending(decision);
    if (context == null || !context.validate(decision)) {
      interaction.fail();
      return interaction;
    }
    try {
      if (!handler.supports(decision)) {
        throw new UnsupportedOperationException(
            "조건과 일치하는 핸들러가 존재하지 않습니다.: "
                + decision.getClass().getSimpleName()
        );
      }

      interaction = executeWith(castService(domainService), castHandler(handler), context, decision);

      if (interaction.isPendingApproval()) {
        interaction.complete();
      }

    } catch (Exception e) {
      interaction.fail();
    }

    return interaction;
  }

  private static <I extends Intent> Interaction executeWith(
      DomainService<I> domainService,
      InteractionHandler<I> handler,
      InteractionContext context,
      Decision<I> decision
  ) {
    return domainService.execute(context, decision, handler);
  }

  @SuppressWarnings("unchecked")
  private static <I extends Intent> InteractionHandler<I> castHandler(InteractionHandler<?> handler) {
    return (InteractionHandler<I>) handler;
  }

  @SuppressWarnings("unchecked")
  private static <I extends Intent> DomainService<I> castService(DomainService<?> service) {
    return (DomainService<I>) service;
  }
}
