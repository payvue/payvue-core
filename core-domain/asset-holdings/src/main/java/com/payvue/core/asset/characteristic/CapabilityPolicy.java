package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetItem;

interface CapabilityPolicy<
    C extends CapabilityCommand,
    T extends AssetCharacteristic
    > {
  boolean validate(AssetItem item, T capability, C command);

  default void validateOrThrow(AssetItem item, T capability, C command) {
    if (!validate(item, capability, command)) {
      throw new IllegalArgumentException("policy validation failed");
    }
  }
}
