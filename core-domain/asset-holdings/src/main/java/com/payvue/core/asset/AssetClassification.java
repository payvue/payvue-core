package com.payvue.core.asset;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssetClassification {

    @EqualsAndHashCode.Include
    private final Category category;

    private AssetClassification(Category category) {
        this.category = category;
    }

    public static AssetClassification of(String value) {
        try {
            Category category = Category.valueOf(value.toUpperCase());
            return new AssetClassification(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "유효하지 않은 자산 카테고리: " + value +
                ". 가능한 값: " + Arrays.toString(Category.values())
            );
        }
    }

    public static AssetClassification cash() {
        return new AssetClassification(Category.CASH);
    }

    public static AssetClassification mileage() {
        return new AssetClassification(Category.MILEAGE);
    }

    public static AssetClassification gift() {
        return new AssetClassification(Category.GIFT);
    }

    public static AssetClassification game() {
        return new AssetClassification(Category.GAME);
    }

    public static AssetClassification music() {
        return new AssetClassification(Category.MUSIC);
    }

    public static AssetClassification coupon() {
        return new AssetClassification(Category.COUPON);
    }

    public static AssetClassification shopping() { return new AssetClassification(Category.SHOPPING); }

    public String getValue() {
        return category.name();
    }

    public boolean isCash() {
        return category == Category.CASH;
    }

    public boolean isMileage() {
        return category == Category.MILEAGE;
    }

    @Override
    public String toString() {
        return category.name();
    }

    public enum Category {
        CASH("현금성 자산"),
        MILEAGE("마일리지"),
        GIFT("기프트카드"),
        GAME("게임 아이템"),
        MUSIC("음원/스트리밍"),
        SHOPPING("쇼핑 상품"),
        COUPON("할인 쿠폰");

        private final String description;

        Category(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
