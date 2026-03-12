package com.payvue.core.hold;

import com.payvue.core.asset.AssetItem;

public interface HoldingSpecification {

    boolean isSatisfiedBy(AssetItem asset);
    default HoldingSpecification and(HoldingSpecification other) {
        return new AndSpecification(this, other);
    }
    default HoldingSpecification or(HoldingSpecification other) {
        return new OrSpecification(this, other);
    }
    default HoldingSpecification not() {
        return new NotSpecification(this);
    }
}
