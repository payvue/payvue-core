package com.payvue.supporting.smartstore.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * SmartStore - 스마트 스토어 Aggregate Root
 * 
 * 커뮤니티 내 스토어를 관리합니다.
 */
@Getter
public class SmartStore {

    private final SmartStoreId id;
    private final CommunityId communityId;
    private StoreProfile profile;
    private StoreStatus status;
    private final List<Inventory> inventories;
    private final Instant createdAt;
    private Instant updatedAt;

    private SmartStore(SmartStoreId id, CommunityId communityId, StoreProfile profile) {
        validateCreation(communityId, profile);
        this.id = id;
        this.communityId = communityId;
        this.profile = profile;
        this.status = StoreStatus.ACTIVE;
        this.inventories = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * 새로운 스마트 스토어를 생성합니다.
     */
    public static SmartStore create(CommunityId communityId, String name, String description) {
        return new SmartStore(
            SmartStoreId.generate(),
            communityId,
            StoreProfile.of(name, description)
        );
    }

    /**
     * 기존 데이터로부터 스토어를 복원합니다. (Repository용)
     */
    public static SmartStore reconstitute(SmartStoreId id, CommunityId communityId,
                                          StoreProfile profile, StoreStatus status,
                                          List<Inventory> inventories,
                                          Instant createdAt, Instant updatedAt) {
        SmartStore store = new SmartStore(id, communityId, profile);
        store.status = status;
        store.inventories.addAll(inventories);
        return store;
    }

    // ==================== 프로필 관리 ====================

    /**
     * 스토어 이름을 변경합니다.
     */
    public void changeName(String newName) {
        this.profile = this.profile.changeName(newName);
        this.updatedAt = Instant.now();
    }

    /**
     * 스토어 설명을 변경합니다.
     */
    public void changeDescription(String newDescription) {
        this.profile = this.profile.changeDescription(newDescription);
        this.updatedAt = Instant.now();
    }

    /**
     * 스토어 로고를 변경합니다.
     */
    public void changeLogo(String logoUrl) {
        this.profile = this.profile.changeLogo(logoUrl);
        this.updatedAt = Instant.now();
    }

    // ==================== 상태 관리 ====================

    /**
     * 스토어를 일시 정지합니다.
     */
    public void suspend() {
        if (status != StoreStatus.ACTIVE) {
            throw new IllegalStateException("활성 상태의 스토어만 정지할 수 있습니다.");
        }
        this.status = StoreStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }

    /**
     * 스토어를 재활성화합니다.
     */
    public void activate() {
        if (status == StoreStatus.CLOSED) {
            throw new IllegalStateException("폐쇄된 스토어는 활성화할 수 없습니다.");
        }
        this.status = StoreStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * 스토어를 폐쇄합니다.
     */
    public void close() {
        this.status = StoreStatus.CLOSED;
        this.updatedAt = Instant.now();
    }

    // ==================== 재고 관리 ====================

    /**
     * 새 상품을 등록합니다.
     */
    public Inventory registerProduct(Product product, int initialQuantity) {
        validateActiveStatus();
        
        if (findInventoryBySku(product.getSku()).isPresent()) {
            throw new IllegalStateException("이미 등록된 상품입니다: " + product.getSku());
        }
        
        Inventory inventory = Inventory.create(product, initialQuantity);
        this.inventories.add(inventory);
        this.updatedAt = Instant.now();
        
        return inventory;
    }

    /**
     * 재고를 추가합니다.
     */
    public void addStock(String sku, int quantity) {
        validateActiveStatus();
        
        Inventory inventory = findInventoryBySku(sku)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + sku));
        
        inventory.addStock(quantity);
        this.updatedAt = Instant.now();
    }

    /**
     * 상품을 예약합니다.
     */
    public ProductReservation reserveProduct(String sku, String reserverId, int quantity) {
        validateActiveStatus();
        
        Inventory inventory = findInventoryBySku(sku)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + sku));
        
        ProductReservation reservation = inventory.reserve(reserverId, quantity);
        this.updatedAt = Instant.now();
        
        return reservation;
    }

    /**
     * 예약을 확정합니다.
     */
    public void confirmReservation(String sku, String reservationId) {
        Inventory inventory = findInventoryBySku(sku)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + sku));
        
        inventory.confirmReservation(reservationId);
        this.updatedAt = Instant.now();
    }

    /**
     * 예약을 취소합니다.
     */
    public void cancelReservation(String sku, String reservationId) {
        Inventory inventory = findInventoryBySku(sku)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + sku));
        
        inventory.cancelReservation(reservationId);
        this.updatedAt = Instant.now();
    }

    // ==================== 조회 메서드 ====================

    /**
     * SKU로 재고를 찾습니다.
     */
    public Optional<Inventory> findInventoryBySku(String sku) {
        return inventories.stream()
            .filter(inv -> inv.getSku().equals(sku))
            .findFirst();
    }

    /**
     * 재고 목록을 반환합니다 (불변).
     */
    public List<Inventory> getInventories() {
        return Collections.unmodifiableList(inventories);
    }

    /**
     * 활성 상태인지 확인합니다.
     */
    public boolean isActive() {
        return status == StoreStatus.ACTIVE;
    }

    /**
     * 스토어 이름을 반환합니다.
     */
    public String getName() {
        return profile.getName();
    }

    // ==================== Private 메서드 ====================

    private void validateActiveStatus() {
        if (!isActive()) {
            throw new IllegalStateException("활성 상태의 스토어에서만 이 작업을 수행할 수 있습니다.");
        }
    }

    private static void validateCreation(CommunityId communityId, StoreProfile profile) {
        if (communityId == null) {
            throw new IllegalArgumentException("커뮤니티 ID는 필수입니다.");
        }
        if (profile == null) {
            throw new IllegalArgumentException("스토어 프로필은 필수입니다.");
        }
    }

    public enum StoreStatus {
        ACTIVE,
        SUSPENDED,
        CLOSED
    }
}
