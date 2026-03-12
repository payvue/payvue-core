package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetItem;

public interface CommandCapabilityHandler<
    T extends AssetCharacteristic,
    C extends CapabilityCommand
    > extends CapabilityHandler<T> {

  CapabilityResult apply(AssetItem item, T capability, C command);

  @Override
  default CapabilityResult apply(AssetItem item, T capability) {
    return CapabilityResult.failure("command required");
  }
}
