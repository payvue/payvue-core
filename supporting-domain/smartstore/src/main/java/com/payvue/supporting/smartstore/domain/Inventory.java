package com.payvue.supporting.smartstore.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Inventory - 재고 엔티티
 * 
 * SmartStore Aggregate의 내부 엔티티로서, 상품의 재고를 관리합니다.
 */
@Getter
public class Inventory {

    private final InventoryId id;
    private Product product;
    private int totalQuantity;
    private int availableQuantity;
    private int reservedQuantity;
    private final List<ProductReservation> reservations;
    private final Instant createdAt;
    private Instant updatedAt;

    private Inventory(InventoryId id, Product product, int initialQuantity) {
        validateProduct(product);
        validateQuantity(initialQuantity);
        this.id = id;
        this.product = product;
        this.totalQuantity = initialQuantity;
        this.availableQuantity = initialQuantity;
        this.reservedQuantity = 0;
        this.reservations = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * 새로운 재고를 생성합니다.
     */
    static Inventory create(Product product, int initialQuantity) {
        return new Inventory(InventoryId.generate(), product, initialQuantity);
    }

    /**
     * 기존 데이터로부터 재고를 복원합니다. (Repository용)
     */
    static Inventory reconstitute(InventoryId id, Product product, int totalQuantity,
                                  int availableQuantity, int reservedQuantity,
                                  List<ProductReservation> reservations,
                                  Instant createdAt, Instant updatedAt) {
        Inventory inventory = new Inventory(id, product, totalQuantity);
        inventory.availableQuantity = availableQuantity;
        inventory.reservedQuantity = reservedQuantity;
        inventory.reservations.addAll(reservations);
        return inventory;
    }

    // ==================== 재고 관리 메서드 ====================

    /**
     * 재고를 추가합니다.
     */
    void addStock(int quantity) {
        validateQuantity(quantity);
        this.totalQuantity += quantity;
        this.availableQuantity += quantity;
        this.updatedAt = Instant.now();
    }

    /**
     * 재고를 차감합니다.
     */
    void deductStock(int quantity) {
        validateQuantity(quantity);
        if (quantity > availableQuantity) {
            throw new IllegalStateException("가용 재고가 부족합니다.");
        }
        this.totalQuantity -= quantity;
        this.availableQuantity -= quantity;
        this.updatedAt = Instant.now();
    }

    /**
     * 재고를 예약합니다.
     */
    ProductReservation reserve(String reserverId, int quantity) {
        validateQuantity(quantity);
        if (quantity > availableQuantity) {
            throw new IllegalStateException("가용 재고가 부족합니다.");
        }

        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;

        ProductReservation reservation = ProductReservation.create(
            this.id, reserverId, quantity
        );
        this.reservations.add(reservation);
        this.updatedAt = Instant.now();

        return reservation;
    }

    /**
     * 예약을 확정합니다 (구매 완료).
     */
    void confirmReservation(String reservationId) {
        ProductReservation reservation = findReservation(reservationId);
        
        this.totalQuantity -= reservation.getQuantity();
        this.reservedQuantity -= reservation.getQuantity();
        
        reservation.confirm();
        this.reservations.remove(reservation);
        this.updatedAt = Instant.now();
    }

    /**
     * 예약을 취소합니다.
     */
    void cancelReservation(String reservationId) {
        ProductReservation reservation = findReservation(reservationId);
        
        this.availableQuantity += reservation.getQuantity();
        this.reservedQuantity -= reservation.getQuantity();
        
        reservation.cancel();
        this.reservations.remove(reservation);
        this.updatedAt = Instant.now();
    }

    /**
     * 상품 정보를 업데이트합니다.
     */
    void updateProduct(Product newProduct) {
        validateProduct(newProduct);
        this.product = newProduct;
        this.updatedAt = Instant.now();
    }

    // ==================== 조회 메서드 ====================

    /**
     * 재고가 있는지 확인합니다.
     */
    public boolean hasStock() {
        return availableQuantity > 0;
    }

    /**
     * 특정 수량을 충족할 수 있는지 확인합니다.
     */
    public boolean canFulfill(int quantity) {
        return availableQuantity >= quantity;
    }

    /**
     * 예약 목록을 반환합니다 (불변).
     */
    public List<ProductReservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    /**
     * 상품 SKU를 반환합니다.
     */
    public String getSku() {
        return product.getSku();
    }

    // ==================== Private 메서드 ====================

    private ProductReservation findReservation(String reservationId) {
        return reservations.stream()
            .filter(r -> r.getId().getValue().equals(reservationId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 필수입니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
    }
}
