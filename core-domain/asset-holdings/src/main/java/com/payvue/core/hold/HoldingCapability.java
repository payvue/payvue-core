package com.payvue.core.hold;

import com.payvue.core.asset.AssetItem;

import java.util.Optional;
import java.util.Set;

public interface HoldingCapability {

    Set<AssetItem> ownedAssetsConformingTo(HoldingSpecification specification);
    Optional<AssetItem> ownedSingleAssetsConformingTo(HoldingSpecification specification);
    OperationResult dealWith(AssetItem item, AssetOperationUnit operation);
    OperationResult dealWith(
            HoldingSpecification specification,
            AssetOperationUnit operation
    );
}
