package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetItem;

public interface CapabilityHandler<T extends AssetCharacteristic> {
  CapabilityResult apply(AssetItem item, T capability);
}
