package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetItem;

class DefaultCapabilityManager implements CapabilityManager {
  private final CapabilityExecutionContext context;

  public DefaultCapabilityManager(CapabilityExecutionContext context) {
    this.context = context;
  }

  @Override
  public <T extends AssetCharacteristic> CapabilityResult process(
      AssetItem item,
      Class<T> capabilityType,
      CapabilityCommand command
  ) {
    return context.execute(item, capabilityType, command);
  }
}
