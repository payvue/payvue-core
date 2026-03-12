package com.payvue.core.asset.characteristic;

import com.payvue.characteristic.AssetCharacteristic;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class CapabilityRegistry {
  private final Map<Class<? extends AssetCharacteristic>, CapabilityHandler<?>> handlers;
  private final Map<CapabilityCommandKey, CommandCapabilityHandler<?, ?>> commandHandlers;

  public CapabilityRegistry() {
    this.handlers = new HashMap<>();
    this.commandHandlers = new HashMap<>();
  }

  public <T extends AssetCharacteristic> void register(
      Class<T> capabilityType,
      CapabilityHandler<T> handler
  ) {
    handlers.put(capabilityType, handler);
  }

  public <T extends AssetCharacteristic, C extends CapabilityCommand> void register(
      Class<T> capabilityType,
      Class<C> commandType,
      CommandCapabilityHandler<T, C> handler
  ) {
    commandHandlers.put(new CapabilityCommandKey(capabilityType, commandType), handler);
  }

  @SuppressWarnings("unchecked")
  public <T extends AssetCharacteristic> Optional<CapabilityHandler<T>> find(
      Class<T> capabilityType
  ) {
    return Optional.ofNullable((CapabilityHandler<T>) handlers.get(capabilityType));
  }

  @SuppressWarnings("unchecked")
  public <T extends AssetCharacteristic>
      Optional<CommandCapabilityHandler<T, CapabilityCommand>> find(
      Class<T> capabilityType,
      Class<? extends CapabilityCommand> commandType
  ) {
    CommandCapabilityHandler<?, ?> direct = commandHandlers.get(
        new CapabilityCommandKey(capabilityType, commandType));
    if (direct != null) {
      return Optional.of((CommandCapabilityHandler<T, CapabilityCommand>) direct);
    }
    for (Map.Entry<CapabilityCommandKey, CommandCapabilityHandler<?, ?>> entry : commandHandlers.entrySet()) {
      CapabilityCommandKey key = entry.getKey();
      if (key.matches(capabilityType, commandType)) {
        return Optional.of((CommandCapabilityHandler<T, CapabilityCommand>) entry.getValue());
      }
    }
    return Optional.empty();
  }

  private static final class CapabilityCommandKey {
    private final Class<? extends AssetCharacteristic> capabilityType;
    private final Class<? extends CapabilityCommand> commandType;

    private CapabilityCommandKey(
        Class<? extends AssetCharacteristic> capabilityType,
        Class<? extends CapabilityCommand> commandType
    ) {
      this.capabilityType = capabilityType;
      this.commandType = commandType;
    }

    private boolean matches(
        Class<? extends AssetCharacteristic> capability,
        Class<? extends CapabilityCommand> command
    ) {
      return capabilityType.equals(capability) && commandType.isAssignableFrom(command);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof CapabilityCommandKey)) {
        return false;
      }
      CapabilityCommandKey other = (CapabilityCommandKey) o;
      return capabilityType.equals(other.capabilityType)
          && commandType.equals(other.commandType);
    }

    @Override
    public int hashCode() {
      return 31 * capabilityType.hashCode() + commandType.hashCode();
    }
  }
}
