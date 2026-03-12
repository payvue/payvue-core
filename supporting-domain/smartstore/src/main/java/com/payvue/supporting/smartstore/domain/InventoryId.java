package com.payvue.supporting.smartstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * InventoryId - 재고 식별자 값 객체
 */
@Getter
@EqualsAndHashCode
public class InventoryId {

    private final String value;

    private InventoryId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("재고 ID는 필수입니다.");
        }
        this.value = value;
    }

    public static InventoryId of(String value) {
        return new InventoryId(value);
    }

    public static InventoryId generate() {
        return new InventoryId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
