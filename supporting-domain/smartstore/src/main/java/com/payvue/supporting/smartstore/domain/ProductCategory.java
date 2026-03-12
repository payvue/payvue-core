package com.payvue.supporting.smartstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * ProductCategory - 상품 카테고리 값 객체
 */
@Getter
@EqualsAndHashCode
public class ProductCategory {

    private final String code;
    private final String name;
    private final String parentCode;

    private ProductCategory(String code, String name, String parentCode) {
        validateCode(code);
        validateName(name);
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

    public static ProductCategory of(String code, String name) {
        return new ProductCategory(code, name, null);
    }

    public static ProductCategory withParent(String code, String name, String parentCode) {
        return new ProductCategory(code, name, parentCode);
    }

    public boolean hasParent() {
        return parentCode != null && !parentCode.isBlank();
    }

    public boolean isRootCategory() {
        return !hasParent();
    }

    private void validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("카테고리 코드는 필수입니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리 이름은 필수입니다.");
        }
    }
}
