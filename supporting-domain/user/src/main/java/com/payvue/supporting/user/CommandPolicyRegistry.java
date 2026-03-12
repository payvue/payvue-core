package com.payvue.supporting.user;

import java.util.HashMap;
import java.util.Map;

final class CommandPolicyRegistry {
  private final Map<String, CommandPolicy> policies = new HashMap<>();

  public CommandPolicyRegistry register(String type, CommandPolicy policy) {
    policies.put(type, policy);
    return this;
  }

  public CommandPolicyRegistry append(String type, CommandPolicy policy) {
    return register(type, policy);
  }

  public CommandPolicy resolve(String type) {
    return policies.get(type);
  }
}
