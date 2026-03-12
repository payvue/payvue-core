package com.payvue.core.hold;

import com.payvue.core.asset.AssetItem;

class AndSpecification implements HoldingSpecification {

    private final HoldingSpecification left;
    private final HoldingSpecification right;

    AndSpecification(HoldingSpecification left, HoldingSpecification right) {
        this.left = java.util.Objects.requireNonNull(left, "left는 필수입니다");
        this.right = java.util.Objects.requireNonNull(right, "right는 필수입니다");
    }

    @Override
    public boolean isSatisfiedBy(AssetItem asset) {
        return left.isSatisfiedBy(asset) && right.isSatisfiedBy(asset);
    }
}
