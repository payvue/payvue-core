package com.payvue.supporting.smartstore.domain;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

/**
 * ProductReservation - 상품 예약 엔티티
 * 
 * Inventory의 내부 엔티티로서, 상품 예약 정보를 관리합니다.
 */
@Getter
public class ProductReservation {

    private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(15);

    private final ProductReservationId id;
    private final InventoryId inventoryId;
    private final String reserverId;
    private final int quantity;
    private ReservationStatus status;
    private final Instant reservedAt;
    private final Instant expiresAt;

    private ProductReservation(ProductReservationId id, InventoryId inventoryId,
                              String reserverId, int quantity, Duration expiration) {
        this.id = id;
        this.inventoryId = inventoryId;
        this.reserverId = reserverId;
        this.quantity = quantity;
        this.status = ReservationStatus.PENDING;
        this.reservedAt = Instant.now();
        this.expiresAt = this.reservedAt.plus(expiration);
    }

    /**
     * 새로운 예약을 생성합니다.
     */
    static ProductReservation create(InventoryId inventoryId, String reserverId, int quantity) {
        return new ProductReservation(
            ProductReservationId.generate(),
            inventoryId,
            reserverId,
            quantity,
            DEFAULT_EXPIRATION
        );
    }

    /**
     * 만료 시간을 지정하여 예약을 생성합니다.
     */
    static ProductReservation createWithExpiration(InventoryId inventoryId, String reserverId,
                                                   int quantity, Duration expiration) {
        return new ProductReservation(
            ProductReservationId.generate(),
            inventoryId,
            reserverId,
            quantity,
            expiration
        );
    }

    /**
     * 예약을 확정합니다.
     */
    void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 예약만 확정할 수 있습니다.");
        }
        if (isExpired()) {
            throw new IllegalStateException("만료된 예약은 확정할 수 없습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    /**
     * 예약을 취소합니다.
     */
    void cancel() {
        if (status == ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("이미 확정된 예약은 취소할 수 없습니다.");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    /**
     * 예약을 만료 처리합니다.
     */
    void expire() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 예약만 만료 처리할 수 있습니다.");
        }
        this.status = ReservationStatus.EXPIRED;
    }

    /**
     * 만료되었는지 확인합니다.
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt) || status == ReservationStatus.EXPIRED;
    }

    /**
     * 유효한 예약인지 확인합니다.
     */
    public boolean isValid() {
        return status == ReservationStatus.PENDING && !isExpired();
    }

    /**
     * 남은 시간을 반환합니다.
     */
    public Duration getRemainingTime() {
        if (isExpired()) {
            return Duration.ZERO;
        }
        return Duration.between(Instant.now(), expiresAt);
    }

    public enum ReservationStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        EXPIRED
    }
}
