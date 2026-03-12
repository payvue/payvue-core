package com.payvue.core.transaction.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetClassification;
import com.payvue.core.asset.AssetItem;
import com.payvue.core.asset.AssetItemId;
import com.payvue.core.asset.AssetStatus;
import com.payvue.core.community.Community;
import com.payvue.core.community.CommunityId;
import com.payvue.core.community.RoleDescriptor;
import com.payvue.core.hold.Holdings;
import com.payvue.core.transaction.command.meta.TransactionMeta;
import com.payvue.shared.Money;
import com.payvue.supporting.user.command.UserId;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TransactionCommand")
class TransactionCommandTest {

  @Test
  @DisplayName("assetType은 대문자로 정규화된다")
  void assetTypeIsUppercased() {
    TransactionCommand base = command(TransactionType.PAYMENT, "CASH");

    TransactionCommand command = new TransactionCommand(
        TransactionType.PAYMENT,
        "cash",
        base.item(),
        Money.of(1000L, "KRW"),
        base.actor(),
        TransactionMeta.empty(),
        base.from(),
        base.to()
    );

    assertThat(command.assetType()).isEqualTo("CASH");
  }

  @Test
  @DisplayName("필수 값이 누락되면 예외가 발생한다")
  void requiresMandatoryValues() {
    TransactionCommand base = command(TransactionType.PAYMENT, "CASH");

    assertThatThrownBy(() -> new TransactionCommand(
        null,
        base.assetType(),
        base.item(),
        base.amount(),
        base.actor(),
        base.meta(),
        base.from(),
        base.to()
    )).isInstanceOf(NullPointerException.class);
  }

  private TransactionCommand command(TransactionType type, String assetType) {
    TestCommunity community = new TestCommunity();
    Holdings holdings = Holdings.create(community);
    UserId actorId = UserId.generate();
    AssetItem item = TestAssetItem.create(community.getId(), actorId);
    Actor actor = new Actor(community, holdings, actorId, new BindingKey(assetType, type));

    return new TransactionCommand(
        type,
        assetType,
        item,
        Money.of(1000L, "KRW"),
        actor,
        TransactionMeta.empty(),
        community,
        community
    );
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
        UserId contributorId,
        AssetClassification category,
        Set<AssetCharacteristic> capabilities,
        AssetStatus status
    ) {
      super(id, name, communityId, contributorId, category, capabilities, status);
    }

    static TestAssetItem create(CommunityId communityId, UserId contributorId) {
      return new TestAssetItem(
          AssetItemId.generate(),
          "asset-item",
          communityId,
          contributorId,
          AssetClassification.cash(),
          Collections.emptySet(),
          AssetStatus.ACTIVE
      );
    }
  }
}
