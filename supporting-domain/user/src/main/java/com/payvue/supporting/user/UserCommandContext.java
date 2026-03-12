package com.payvue.supporting.user;

import com.payvue.supporting.user.command.UserRepository;

record UserCommandContext(
    UserRepository repository,
    java.util.Map<String, CommandPolicy> policiesByType,
    java.util.Map<String, FeatureDefaultsRegistry> featureDefaultsByType,
    UserLookup<?> lookup
) {
  public CommandPolicy policy(String type) {
    if (type == null || type.isBlank()) {
      return null;
    }
    return policiesByType.get(type);
  }

  public FeatureDefaultsRegistry featureDefaults(String type) {
    if (type == null || type.isBlank()) {
      return null;
    }
    return featureDefaultsByType.get(type);
  }
}
