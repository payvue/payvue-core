package com.payvue.supporting.user;

import java.util.List;

final class PolicyChain implements CommandPolicy {
  private final List<CommandPolicy> policies;

  private PolicyChain(List<CommandPolicy> policies) {
    this.policies = policies;
  }

  public static PolicyChain of(CommandPolicy... policies) {
    return new PolicyChain(List.of(policies));
  }

  @Override
  public boolean validate(UserCommand command) {
    for (CommandPolicy policy : policies) {
      boolean isvalid = policy.validate(command);
      if(!isvalid)
        return isvalid;
    }
    return true;
  }
}
