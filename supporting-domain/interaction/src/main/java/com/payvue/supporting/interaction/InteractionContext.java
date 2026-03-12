package com.payvue.supporting.interaction;

interface InteractionContext {

  Interaction execute(Decision<?> decision);

  boolean supports(Decision<?> decision, DomainService service);

  boolean validate(Decision<?> decision);
}
