package com.payvue.core.transaction.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.common.status.ExecutionState;
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
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PolicyAwareHandlerChain")
class PolicyAwareHandlerChainTest {

  @Test
  @DisplayName("policy가 false이면 defaults만 실행된다")
  void defaultsOnlyWhenPolicyFalse() {
    CountingHandler defaults = new CountingHandler(ExecutionState.COMPLETED);
    CountingDecorator followUp = new CountingDecorator();

    PolicyAwareHandlerChain chain = new PolicyAwareHandlerChain(
        (command, primary) -> false,
        defaults,
        java.util.List.of(followUp)
    );

    Transaction result = chain.handle(command(TransactionType.PAYMENT));

    assertThat(defaults.calls()).isEqualTo(1);
    assertThat(followUp.calls()).isZero();
    assertThat(result.interactionState()).isEqualTo(ExecutionState.COMPLETED);
  }

  @Test
  @DisplayName("policy가 true이면 모든 데코레이터를 적용한다")
  void mergesWhenPolicyTrue() {
    CountingHandler defaults = new CountingHandler(ExecutionState.COMPLETED);
    CountingDecorator followUp = new CountingDecorator();

    PolicyAwareHandlerChain chain = new PolicyAwareHandlerChain(
        (command, primary) -> true,
        defaults,
        java.util.List.of(followUp)
    );

    Transaction result = chain.handle(command(TransactionType.PAYMENT));

    assertThat(defaults.calls()).isEqualTo(1);
    assertThat(followUp.calls()).isEqualTo(1);
    assertThat(result.interactionState()).isEqualTo(ExecutionState.COMPLETED);
  }

  private static TransactionCommand command(TransactionType type) {
    TestCommunity community = new TestCommunity();
    Holdings holdings = Holdings.create(community);
    UserId actorId = UserId.generate();
    AssetItem item = TestAssetItem.create(community.getId(), actorId);
    Actor actor = new Actor(community, holdings, actorId, new BindingKey("CASH", type));

    return new TransactionCommand(
        type,
        "CASH",
        item,
        Money.of(1000L, "KRW"),
        actor,
        TransactionMeta.empty(),
        community,
        community
    );
  }

  private static Transaction transactionWithState(ExecutionState state) {
    TransactionCommand command = command(TransactionType.PAYMENT);
    return Transaction.reconstitute(
        TransactionId.generate(),
        command.type(),
        command.assetType(),
        command.from(),
        command.to(),
        command.item(),
        command.amount().toLong(),
        command.actor().actorId(),
        command.item().getName(),
        command.item().getCategory().getValue(),
        state,
        Instant.now(),
        command.meta()
    );
  }

  private static final class CountingHandler implements TransactionHandler {
    private final ExecutionState state;
    private final AtomicInteger calls = new AtomicInteger();

    private CountingHandler(ExecutionState state) {
      this.state = state;
    }

    @Override
    public Transaction handle(TransactionCommand command) {
      calls.incrementAndGet();
      return transactionWithState(state);
    }

    int calls() {
      return calls.get();
    }
  }

  private static final class CountingDecorator implements TransactionDecorator {
    private final AtomicInteger calls = new AtomicInteger();

    @Override
    public Transaction decorate(Transaction transaction) {
      calls.incrementAndGet();
      return transaction;
    }

    int calls() {
      return calls.get();
    }
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
