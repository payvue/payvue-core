package com.payvue.core.hold;

import com.payvue.core.asset.AssetItemId;

public class OperationResult {

    private final AssetOperationOutcome outcome;

    private OperationResult(AssetOperationOutcome outcome) {
        this.outcome = outcome;
    }

    public static OperationResult success(
            AssetItemId assetId,
            String assetName,
            String message
    ) {
        return new OperationResult(
                AssetOperationOutcome.success(assetId, assetName, message)
        );
    }

    public static OperationResult failure(
            AssetItemId assetId,
            String assetName,
            String reason
    ) {
        return new OperationResult(
                AssetOperationOutcome.failure(assetId, assetName, reason)
        );
    }

    public AssetOperationOutcome outcome() {
        return outcome;
    }

    public boolean isSuccess() {
        return outcome.success();
    }

    public boolean isFailure() {
        return !outcome.success();
    }

    public String message() {
        return outcome.message();
    }

    public record AssetOperationOutcome(
            AssetItemId assetId,
            String assetName,
            boolean success,
            String message
    ) {
        public static AssetOperationOutcome success(
                AssetItemId id,
                String name,
                String msg
        ) {
            return new AssetOperationOutcome(id, name, true, msg);
        }

        public static AssetOperationOutcome failure(
                AssetItemId id,
                String name,
                String reason
        ) {
            return new AssetOperationOutcome(id, name, false, reason);
        }
    }
}
