package com.payvue.supporting.user;

import com.payvue.supporting.user.command.User;
import com.payvue.supporting.user.command.FeatureKey;
import com.payvue.supporting.user.command.FeaturePreferences;

import java.util.HashMap;
import java.util.Map;

final class FeatureDefaultsRegistry {
  private final Map<FeatureKey, FeaturePreference> defaults;

  private FeatureDefaultsRegistry(Map<FeatureKey, FeaturePreference> defaults) {
    this.defaults = defaults;
  }

  public static Builder builder() {
    return new Builder();
  }

  public FeaturePreferences applyDefaults(FeaturePreferences base) {
    FeaturePreferences result = base;
    for (FeaturePreference pref : defaults.values()) {
      result = result.apply(pref);
    }
    return result;
  }

  public void applyTo(User user) {
    if (user == null) {
      return;
    }
    FeaturePreferences defaults = applyDefaults(FeaturePreferences.empty());
    for (FeaturePreference preference : defaults.getValues().values()) {
      user.applyFeaturePreference(preference);
    }
  }

  public static final class Builder {
    private final Map<FeatureKey, FeaturePreference> defaults = new HashMap<>();

    public Builder register(FeaturePreference preference) {
      defaults.put(preference.key(), preference);
      return this;
    }

    public FeatureDefaultsRegistry build() {
      return new FeatureDefaultsRegistry(Map.copyOf(defaults));
    }
  }
}
