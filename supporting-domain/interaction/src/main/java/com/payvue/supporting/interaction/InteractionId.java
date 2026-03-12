package com.payvue.supporting.interaction;

import java.util.Objects;
import java.util.UUID;

public class InteractionId {
    
    private final String value;
    
    private InteractionId(String value) {
        this.value = Objects.requireNonNull(value, "value는 필수입니다");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value는 비어있을 수 없습니다");
        }
    }

    public static InteractionId generate() {
        return new InteractionId(UUID.randomUUID().toString());
    }
    public static InteractionId of(String value) {
        return new InteractionId(value);
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionId that = (InteractionId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
