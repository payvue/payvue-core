package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetItem;

import java.util.Optional;

class CapabilityExecutionContext {
  private final CapabilityRegistry registry;

  private CapabilityExecutionContext(CapabilityRegistry registry) {
    this.registry = registry;
  }

  public static Builder builder() {
    return new Builder();
  }

  public <T extends AssetCharacteristic> CapabilityResult execute(
      AssetItem item,
      Class<T> capabilityType,
      CapabilityCommand command
  ) {
    if (item == null || capabilityType == null) {
      return CapabilityResult.failure("invalid arguments");
    }
    Optional<T> capability = item.getCapability(capabilityType);
    if (capability.isEmpty()) {
      return CapabilityResult.failure("capability not found");
    }
    if (command == null) {
      Optional<CapabilityHandler<T>> handler = registry.find(capabilityType);
      if (handler.isEmpty()) {
        return CapabilityResult.failure("handler not found");
      }
      return handler.get().apply(item, capability.get());
    }
    Optional<CommandCapabilityHandler<T, CapabilityCommand>> commandHandler =
        registry.find(capabilityType, command.getClass());
    if (commandHandler.isPresent()) {
      return commandHandler.get().apply(item, capability.get(), command);
    }
    return CapabilityResult.failure("command not supported");
  }

  public static class Builder {
    private final CapabilityRegistry registry = new CapabilityRegistry();

    public <T extends AssetCharacteristic> Builder register(
        Class<T> capabilityType,
        CapabilityHandler<T> handler
    ) {
      registry.register(capabilityType, handler);
      return this;
    }

    public <T extends AssetCharacteristic, C extends CapabilityCommand> Builder register(
        Class<T> capabilityType,
        Class<C> commandType,
        CommandCapabilityHandler<T, C> handler
    ) {
      registry.register(capabilityType, commandType, handler);
      return this;
    }

    public CapabilityExecutionContext build() {
      return new CapabilityExecutionContext(registry);
    }
  }
}
