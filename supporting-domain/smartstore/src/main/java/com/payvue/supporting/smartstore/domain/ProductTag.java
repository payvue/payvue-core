package com.payvue.supporting.smartstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * ProductTag - 상품 태그 값 객체
 */
@Getter
@EqualsAndHashCode
public class ProductTag {

    private final String value;

    private ProductTag(String value) {
        validateValue(value);
        this.value = value.toLowerCase().trim();
    }

    public static ProductTag of(String value) {
        return new ProductTag(value);
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("태그 값은 필수입니다.");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("태그는 50자를 초과할 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
