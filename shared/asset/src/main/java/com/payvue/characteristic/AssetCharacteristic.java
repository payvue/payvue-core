package com.payvue.characteristic;

public sealed interface AssetCharacteristic
        permits Ownable, Consumable, HasBalance, Acquirable {
}
