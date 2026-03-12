package com.payvue.supporting.smartstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * SmartStoreId - 스마트 스토어 식별자 값 객체
 */
@Getter
@EqualsAndHashCode
public class SmartStoreId {

    private final String value;

    private SmartStoreId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("스마트 스토어 ID는 필수입니다.");
        }
        this.value = value;
    }

    public static SmartStoreId of(String value) {
        return new SmartStoreId(value);
    }

    public static SmartStoreId generate() {
        return new SmartStoreId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
