package com.payvue.core.transaction.command.meta;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class TransactionMeta {

  private final EnumMap<TransactionMetaKey, String> values;

  private TransactionMeta(EnumMap<TransactionMetaKey, String> values) {
    this.values = values;
  }

  public static TransactionMeta of(Map<TransactionMetaKey, String> input) {
    Objects.requireNonNull(input, "input은 필수입니다.");
    EnumMap<TransactionMetaKey, String> map = new EnumMap<>(TransactionMetaKey.class);
    map.putAll(input);
    return new TransactionMeta(map);
  }

  public static TransactionMeta fromRawMap(Map<String, String> input) {
    if (input == null || input.isEmpty()) {
      return empty();
    }
    EnumMap<TransactionMetaKey, String> map = new EnumMap<>(TransactionMetaKey.class);
    for (Map.Entry<String, String> entry : input.entrySet()) {
      TransactionMetaKey key = TransactionMetaKey.valueOf(entry.getKey());
      map.put(key, entry.getValue());
    }
    return new TransactionMeta(map);
  }

  public static TransactionMeta empty() {
    return new TransactionMeta(new EnumMap<>(TransactionMetaKey.class));
  }

  public String get(TransactionMetaKey key) {
    return values.get(key);
  }

  public boolean contains(TransactionMetaKey key) {
    return values.containsKey(key);
  }

  public Map<TransactionMetaKey, String> asMap() {
    return Map.copyOf(values);
  }

  public Map<String, String> toRawMap() {
    EnumMap<TransactionMetaKey, String> map = new EnumMap<>(values);
    return map.entrySet().stream()
        .collect(java.util.stream.Collectors.toMap(
            entry -> entry.getKey().name(),
            Map.Entry::getValue
        ));
  }

  public TransactionMeta merge(TransactionMeta other) {
    if (other == null) {
      return this;
    }
    EnumMap<TransactionMetaKey, String> merged = new EnumMap<>(TransactionMetaKey.class);
    merged.putAll(this.values);
    merged.putAll(other.values);
    return new TransactionMeta(merged);
  }
}
