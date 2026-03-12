package com.payvue.core.hold;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.core.asset.AssetItem;
import com.payvue.core.community.Community;
import com.payvue.core.community.CommunityId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Holdings extends AggregateRoot<HoldingsId, Holdings>
        implements Identifiable<HoldingsId>, HoldingCapability {

    @Getter
    @EqualsAndHashCode.Include
    private final HoldingsId id;
    @Getter
    private final CommunityId communityId;
    private final List<AssetItem> assets;

    private Holdings(
            HoldingsId id,
            CommunityId communityId,
            List<AssetItem> assets
    ) {
        this.id = Objects.requireNonNull(id, "id는 필수입니다");
        this.communityId = Objects.requireNonNull(communityId, "communityId는 필수입니다");
        this.assets = new ArrayList<>(assets);
        validateInvariants();
    }

    public static Holdings create(Community community) {
        return new Holdings(
                HoldingsId.generate(),
                community.getId(),
                new ArrayList<>()
        );
    }

    public static Holdings create(Community community, List<AssetItem> initialAssets) {
        return new Holdings(
                HoldingsId.generate(),
                community.getId(),
                initialAssets
        );
    }

    @Override
    public Set<AssetItem> ownedAssetsConformingTo(HoldingSpecification specification) {
        java.util.Objects.requireNonNull(specification, "specification은 필수입니다");
        return assets.stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<AssetItem> ownedSingleAssetsConformingTo(HoldingSpecification specification) {
        java.util.Objects.requireNonNull(specification, "specification은 필수입니다");
        return assets.stream()
                .filter(specification::isSatisfiedBy)
                .findFirst();
    }

    @Override
    public OperationResult dealWith(AssetItem item, AssetOperationUnit operation) {
        java.util.Objects.requireNonNull(item, "item은 필수입니다");
        java.util.Objects.requireNonNull(operation, "operation은 필수입니다");
        return operation.execute(item);
    }

    @Override
    public OperationResult dealWith(
            HoldingSpecification specification,
            AssetOperationUnit operation
    ) {
        java.util.Objects.requireNonNull(specification, "specification은 필수입니다");
        java.util.Objects.requireNonNull(operation, "operation은 필수입니다");
        Optional<AssetItem> target = assets.stream()
                .filter(specification::isSatisfiedBy)
                .findFirst();

        if(target.isPresent()) {
            return operation.execute(target.get());
        }
         return OperationResult.failure(null, null, "자산을 식별할 수 없습니다.");
    }

    private void validateInvariants() {
        for (AssetItem asset : assets) {
            validateCommunityOwnership(asset);
        }
    }

    private void validateCommunityOwnership(AssetItem asset) {
        if (!asset.getCommunityId().equals(this.communityId)) {
            throw new IllegalArgumentException(
                    "자산의 소유 커뮤니티가 일치하지 않습니다. " +
                            "Holdings: " + this.communityId + ", " +
                            "Asset: " + asset.getCommunityId()
            );
        }
    }

    public List<AssetItem> getAssets() {
        return this.assets;
    }

    public static Holdings reconstitute(
            HoldingsId id,
            CommunityId communityId,
            List<AssetItem> assets
    ) {
        return new Holdings(id, communityId, assets);
    }
}
