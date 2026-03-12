package com.payvue.core.asset;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.community.CommunityId;
import com.payvue.supporting.user.command.UserId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.*;


@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AssetItem {

    @EqualsAndHashCode.Include
    private final AssetItemId id;
    private final String name;
    private CommunityId communityId;
    private final UserId contributorId;
    private final AssetClassification category;
    private final Set<AssetCharacteristic> capabilities;
    private AssetStatus status;

    protected AssetItem(
            AssetItemId id,
            String name,
            CommunityId communityId,
            UserId contributorId,
            AssetClassification category,
            Set<AssetCharacteristic> capabilities,
            AssetStatus status
    ) {
        this.id = Objects.requireNonNull(id, "id는 필수입니다");
        this.name = Objects.requireNonNull(name, "name은 필수입니다");
        this.communityId = Objects.requireNonNull(communityId, "communityId는 필수입니다");
        this.contributorId = contributorId;
        this.category = Objects.requireNonNull(category, "category는 필수입니다");
        this.capabilities = new HashSet<>(capabilities);
        this.status = Objects.requireNonNull(status, "status는 필수입니다");
    }
    public AssetItem with(AssetCharacteristic capability) {
        Objects.requireNonNull(capability, "capability는 필수입니다");
        this.capabilities.add(capability);
        return this;
    }

    public Set<AssetCharacteristic> getCapabilities() {
        return Collections.unmodifiableSet(capabilities);
    }

    public <T extends AssetCharacteristic> Optional<T> getCapability(Class<T> capabilityType) {
        return capabilities.stream()
                .filter(capabilityType::isInstance)
                .map(capabilityType::cast)
                .findFirst();
    }

    public boolean hasCapability(Class<? extends AssetCharacteristic> capabilityType) {
        return capabilities.stream()
                .anyMatch(capabilityType::isInstance);
    }

    public boolean isActive() {
        return status.isActive();
    }

    public void use() {
        this.status = status.use();
    }

    public void expire() {
        this.status = status.expire();
    }

    public void suspend() {
        this.status = status.suspend();
    }

    public void activate() {
        this.status = status.activate();
    }

    public void transferToCommunity(CommunityId communityId) {
        this.communityId = Objects.requireNonNull(communityId, "communityId는 필수입니다");
    }

    public static AssetItem reconstitute(
            AssetItemId id,
            String name,
            CommunityId communityId,
            UserId contributorId,
            AssetClassification category,
            Set<AssetCharacteristic> capabilities,
            Instant createdAt,
            AssetStatus status
    ) {
        throw new UnsupportedOperationException("하위 클래스의 reconstitute 사용 필요");
    }
}
