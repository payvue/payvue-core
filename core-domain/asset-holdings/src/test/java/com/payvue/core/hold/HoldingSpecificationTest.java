package com.payvue.core.hold;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.core.asset.AssetClassification;
import com.payvue.core.asset.AssetItem;
import com.payvue.core.asset.AssetItemId;
import com.payvue.core.asset.AssetStatus;
import com.payvue.core.community.CommunityId;
import com.payvue.supporting.user.command.UserId;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HoldingSpecification")
class HoldingSpecificationTest {

  @Test
  @DisplayName("and 조합은 모두 만족해야 한다")
  void andSpecRequiresAll() {
    AssetItem asset = TestAssetItem.create("alpha");
    HoldingSpecification left = a -> a.getName().equals("alpha");
    HoldingSpecification right = a -> a.getStatus().isActive();

    assertThat(left.and(right).isSatisfiedBy(asset)).isTrue();
  }

  @Test
  @DisplayName("or 조합은 하나만 만족해도 통과한다")
  void orSpecRequiresAny() {
    AssetItem asset = TestAssetItem.create("alpha");
    HoldingSpecification left = a -> a.getName().equals("beta");
    HoldingSpecification right = a -> a.getStatus().isActive();

    assertThat(left.or(right).isSatisfiedBy(asset)).isTrue();
  }

  @Test
  @DisplayName("not 조합은 결과를 반전한다")
  void notSpecInvertsResult() {
    AssetItem asset = TestAssetItem.create("alpha");
    HoldingSpecification spec = a -> a.getName().equals("alpha");

    assertThat(spec.not().isSatisfiedBy(asset)).isFalse();
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

    static TestAssetItem create(String name) {
      return new TestAssetItem(
          AssetItemId.generate(),
          name,
          CommunityId.generate(),
          UserId.generate()
      );
    }
  }
}
