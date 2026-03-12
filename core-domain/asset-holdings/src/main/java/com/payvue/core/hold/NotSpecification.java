package com.payvue.core.hold;

import com.payvue.core.asset.AssetItem;

class NotSpecification implements HoldingSpecification {

    private final HoldingSpecification spec;

    NotSpecification(HoldingSpecification spec) {
        this.spec = java.util.Objects.requireNonNull(spec, "spec은 필수입니다");
    }

    @Override
    public boolean isSatisfiedBy(AssetItem asset) {
        return !spec.isSatisfiedBy(asset);
    }
}
