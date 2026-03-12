package com.payvue.supporting.smartstore.domain;

import com.payvue.shared.domain.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Product - 상품 값 객체
 */
@Getter
@EqualsAndHashCode
public class Product {

    private final String sku;
    private final String name;
    private final String description;
    private final Money price;
    private final ProductCategory category;
    private final Set<ProductTag> tags;
    private final String imageUrl;
    private final boolean active;

    private Product(String sku, String name, String description, Money price,
                   ProductCategory category, Set<ProductTag> tags, String imageUrl, boolean active) {
        validateSku(sku);
        validateName(name);
        validatePrice(price);
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
        this.imageUrl = imageUrl;
        this.active = active;
    }

    public static Product create(String sku, String name, String description,
                                 Money price, ProductCategory category) {
        return new Product(sku, name, description, price, category, null, null, true);
    }

    public static Product createFull(String sku, String name, String description,
                                     Money price, ProductCategory category,
                                     Set<ProductTag> tags, String imageUrl) {
        return new Product(sku, name, description, price, category, tags, imageUrl, true);
    }

    public Product changePrice(Money newPrice) {
        return new Product(this.sku, this.name, this.description, newPrice,
                          this.category, this.tags, this.imageUrl, this.active);
    }

    public Product changeName(String newName) {
        return new Product(this.sku, newName, this.description, this.price,
                          this.category, this.tags, this.imageUrl, this.active);
    }

    public Product addTag(ProductTag tag) {
        Set<ProductTag> newTags = new HashSet<>(this.tags);
        newTags.add(tag);
        return new Product(this.sku, this.name, this.description, this.price,
                          this.category, newTags, this.imageUrl, this.active);
    }

    public Product removeTag(ProductTag tag) {
        Set<ProductTag> newTags = new HashSet<>(this.tags);
        newTags.remove(tag);
        return new Product(this.sku, this.name, this.description, this.price,
                          this.category, newTags, this.imageUrl, this.active);
    }

    public Product activate() {
        return new Product(this.sku, this.name, this.description, this.price,
                          this.category, this.tags, this.imageUrl, true);
    }

    public Product deactivate() {
        return new Product(this.sku, this.name, this.description, this.price,
                          this.category, this.tags, this.imageUrl, false);
    }

    public Set<ProductTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public boolean hasTag(ProductTag tag) {
        return tags.contains(tag);
    }

    private void validateSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("상품 SKU는 필수입니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
    }

    private void validatePrice(Money price) {
        if (price == null || price.isNegative()) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }
    }
}
