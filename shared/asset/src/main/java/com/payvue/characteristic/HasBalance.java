package com.payvue.characteristic;

import com.payvue.shared.Money;
import lombok.EqualsAndHashCode;

import java.util.Objects;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public non-sealed class HasBalance implements AssetCharacteristic {

    @EqualsAndHashCode.Include
    private Money balance;

    private HasBalance(Money balance) {
        this.balance = Objects.requireNonNull(balance, "balance는 필수입니다");
        if (balance.isNegative()) {
            throw new IllegalArgumentException("잔액은 음수일 수 없습니다");
        }
    }

    public static HasBalance of(Money balance) {
        return new HasBalance(balance);
    }

    public Money balance() {
        return balance;
    }

    public void deduct(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new IllegalArgumentException(
                "잔액 부족: 현재 " + balance + ", 차감 시도 " + amount
            );
        }
        this.balance = balance.subtract(amount);
    }

    public void add(Money amount) {
        this.balance = balance.add(amount);
    }
}
