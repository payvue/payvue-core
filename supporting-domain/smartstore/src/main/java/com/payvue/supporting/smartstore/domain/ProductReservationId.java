package com.payvue.supporting.smartstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * ProductReservationId - 상품 예약 식별자 값 객체
 */
@Getter
@EqualsAndHashCode
public class ProductReservationId {

    private final String value;

    private ProductReservationId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("예약 ID는 필수입니다.");
        }
        this.value = value;
    }

    public static ProductReservationId of(String value) {
        return new ProductReservationId(value);
    }

    public static ProductReservationId generate() {
        return new ProductReservationId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
