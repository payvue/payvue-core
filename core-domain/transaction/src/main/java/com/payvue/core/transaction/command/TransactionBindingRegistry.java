package com.payvue.core.transaction.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class TransactionBindingRegistry {

  private final Map<BindingKey, TransactionHandler> bindings;

  private TransactionBindingRegistry(Map<BindingKey, TransactionHandler> bindings) {
    this.bindings = bindings;
  }

  public static Builder builder() {
    return new Builder();
  }

  public TransactionBinding resolve(String assetType, TransactionType type) {
    TransactionHandler handler = bindings.get(new BindingKey(assetType, type));
    if (handler == null) {
      throw new IllegalArgumentException("지원하지 않는 트랜잭션 조합입니다: " + assetType + "/" + type);
    }
    return new DefaultTransactionBinding(new BindingKey(assetType, type), handler);
  }

  public static final class Builder {
    private final Map<BindingKey, TransactionHandler> bindings = new HashMap<>();

    public Builder bind(BindingKey key, TransactionHandler handler) {
      Objects.requireNonNull(key, "key는 필수입니다.");
      Objects.requireNonNull(handler, "handler는 필수입니다.");
      if (bindings.containsKey(key)) {
        throw new IllegalArgumentException("중복 바인딩이 등록되었습니다: " + key);
      }
      bindings.put(key, handler);
      return this;
    }

    public Builder bind(Set<BindingKey> keys, TransactionHandler handler) {
      Objects.requireNonNull(keys, "keys는 필수입니다.");
      for (BindingKey key : keys) {
        bind(key, handler);
      }
      return this;
    }

    public TransactionBindingRegistry build() {
      return new TransactionBindingRegistry(Map.copyOf(bindings));
    }
  }
}
