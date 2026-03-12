package com.payvue.core.hold;

import com.payvue.core.asset.AssetItem;

import java.util.ArrayList;
import java.util.List;


public interface AssetOperationUnit {

    OperationResult execute(AssetItem asset);
    default List<OperationResult> execute(List<AssetItem> assets) {
        List<OperationResult> results = new ArrayList<>();
        
        for (AssetItem asset : assets) {
            try {
                OperationResult result = execute(asset);
                results.add(result);
            } catch (Exception e) {
                results.add(OperationResult.failure(
                    asset.getId(),
                    asset.getName(),
                    "작업 실패: " + e.getMessage()
                ));
            }
        }
        
        return results;
    }

    OperationType getType();
    String getDescription();
}
