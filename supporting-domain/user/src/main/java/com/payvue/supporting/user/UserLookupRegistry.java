package com.payvue.supporting.user;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final class UserLookupRegistry {
  private final Map<Class<?>, Entry<?>> registry = new HashMap<>();

  public <T, R> UserLookupRegistry register(
      Class<T> type,
      Function<UserLookupKey<T>, R> handler
  ) {
    registry.put(type, new Entry<>(type, handler));
    return this;
  }

  public Object lookup(UserLookupKey<?> key) {
    if (key == null) {
      return null;
    }
    Entry<?> entry = registry.get(key.type());
    if (entry == null) {
      return null;
    }
    return apply(entry, key);
  }

  private record Entry<T>(
      Class<T> type,
      Function<UserLookupKey<T>, ?> handler
  ) {}

  @SuppressWarnings("unchecked")
  private <T> Object apply(Entry<T> entry, UserLookupKey<?> key) {
    return entry.handler().apply((UserLookupKey<T>) key);
  }
}
