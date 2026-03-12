package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetItem;

public interface CapabilityManager {
  <T extends AssetCharacteristic> CapabilityResult process(
      AssetItem item,
      Class<T> capabilityType,
      CapabilityCommand command
  );
}
