package com.payvue.core.hold;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.core.asset.AssetClassification;
import com.payvue.core.asset.AssetItem;
import com.payvue.core.asset.AssetItemId;
import com.payvue.core.asset.AssetStatus;
import com.payvue.core.community.Community;
import com.payvue.core.community.CommunityId;
import com.payvue.core.community.RoleDescriptor;
import com.payvue.supporting.user.command.UserId;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Holdings")
class HoldingsTest {

  @Test
  @DisplayName("다른 커뮤니티 자산을 포함하면 예외가 발생한다")
  void rejectsAssetsFromDifferentCommunity() {
    TestCommunity community = new TestCommunity();
    TestCommunity other = new TestCommunity();
    AssetItem asset = TestAssetItem.create(other.getId());

    assertThatThrownBy(() -> Holdings.create(community, List.of(asset)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("스펙에 부합하는 자산만 조회된다")
  void filtersAssetsBySpecification() {
    TestCommunity community = new TestCommunity();
    AssetItem first = TestAssetItem.create(community.getId(), "alpha");
    AssetItem second = TestAssetItem.create(community.getId(), "beta");
    Holdings holdings = Holdings.create(community, List.of(first, second));

    HoldingSpecification spec = asset -> asset.getName().equals("alpha");

    Set<AssetItem> result = holdings.ownedAssetsConformingTo(spec);

    assertThat(result).containsExactly(first);
  }

  @Test
  @DisplayName("단일 스펙 조회는 첫 번째 일치 항목을 반환한다")
  void returnsSingleAssetWhenMatched() {
    TestCommunity community = new TestCommunity();
    AssetItem first = TestAssetItem.create(community.getId(), "alpha");
    AssetItem second = TestAssetItem.create(community.getId(), "beta");
    Holdings holdings = Holdings.create(community, List.of(first, second));

    HoldingSpecification spec = asset -> asset.getName().equals("beta");

    assertThat(holdings.ownedSingleAssetsConformingTo(spec)).contains(second);
  }

  @Test
  @DisplayName("자산에 대한 오퍼레이션이 실행된다")
  void executesOperationUnit() {
    TestCommunity community = new TestCommunity();
    AssetItem asset = TestAssetItem.create(community.getId(), "alpha");
    Holdings holdings = Holdings.create(community, List.of(asset));

    AssetOperationUnit operation = new AssetOperationUnit() {
      @Override
      public OperationResult execute(AssetItem asset) {
        return OperationResult.success(asset.getId(), asset.getName(), "ok");
      }

      @Override
      public OperationType getType() {
        return OperationType.ACQUIRE;
      }

      @Override
      public String getDescription() {
        return "test";
      }
    };

    OperationResult result = holdings.dealWith(asset, operation);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.message()).isEqualTo("ok");
  }

  static final class TestCommunity extends Community {
    private static final RoleDescriptor ADMIN_ROLE = new TestRoleDescriptor();

    TestCommunity() {
      super("test-community", "test", UUID.randomUUID(), ADMIN_ROLE);
    }

    @Override
    public String getCommunityType() {
      return "TEST";
    }
  }

  static final class TestRoleDescriptor implements RoleDescriptor {
    @Override
    public String getName() {
      return "ADMIN";
    }

    @Override
    public boolean isAdmin() {
      return true;
    }
  }

  static final class TestAssetItem extends AssetItem {
    private TestAssetItem(
        AssetItemId id,
        String name,
        CommunityId communityId,
        UserId contributorId
    ) {
      super(
          id,
          name,
          communityId,
          contributorId,
          AssetClassification.cash(),
          Set.of(),
          AssetStatus.ACTIVE
      );
    }

    static TestAssetItem create(CommunityId communityId) {
      return create(communityId, "asset");
    }

    static TestAssetItem create(CommunityId communityId, String name) {
      return new TestAssetItem(
          AssetItemId.generate(),
          name,
          communityId,
          UserId.generate()
      );
    }
  }
}
