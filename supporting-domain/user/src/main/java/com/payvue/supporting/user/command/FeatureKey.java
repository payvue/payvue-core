package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * FeatureKey - 기능 설정 키 값 객체
 */
@Getter
@EqualsAndHashCode
public class FeatureKey {

    private final String value;

    private FeatureKey(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("기능 키는 필수입니다.");
        }
        this.value = value;
    }

    public static FeatureKey of(String value) {
        return new FeatureKey(value);
    }
}
