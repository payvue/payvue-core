package com.payvue.characteristic;


public non-sealed class Consumable implements AssetCharacteristic {

    private boolean reusable;
    private int quantity;

    private Consumable(boolean reusable, int quantity) {
        this.reusable = reusable;
        this.quantity = quantity;
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다");
        }
    }

    public static Consumable multipleUse(int quantity) {
        return new Consumable(true, quantity);
    }

    public static Consumable multipleUse() {
        return new Consumable(true, 1);
    }

    public static Consumable singleUse(int quantity) {
        return new Consumable(false, quantity);
    }

    public static Consumable singleUse() {
        return new Consumable(false, 1);
    }

    public boolean reusable() {
        return reusable;
    }

    public int quantity() {
        return quantity;
    }

    public boolean canConsume() {
        return quantity > 0;
    }

    public boolean hasQuantity() {
        return quantity > 0;
    }

    public void increaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("증가량은 음수일 수 없습니다");
        }
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("차감량은 음수일 수 없습니다");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException(
                "수량 부족: 현재 " + quantity + ", 차감 시도 " + amount
            );
        }
        this.quantity -= amount;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다");
        }
        this.quantity = quantity;
    }
}
