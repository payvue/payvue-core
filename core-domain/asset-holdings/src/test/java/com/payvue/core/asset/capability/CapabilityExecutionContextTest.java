package com.payvue.core.asset.capability;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.characteristic.AssetCharacteristic;
import com.payvue.core.asset.AssetClassification;
import com.payvue.core.asset.AssetItem;
import com.payvue.core.asset.AssetItemId;
import com.payvue.core.asset.AssetStatus;
import com.payvue.core.asset.characteristic.*;
import com.payvue.core.community.CommunityId;
import com.payvue.supporting.user.command.UserId;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CapabilityExecutionContext")
class CapabilityExecutionContextTest {

  @Test
  @DisplayName("등록된 커맨드 핸들러가 실행된다")
  void executesRegisteredCommandHandler() {
    CapabilityExecutionContext context = CapabilityExecutionContext.builder()
        .register(TestCapability.class, TestCommand.class, new TestCommandHandler())
        .build();
    AssetItem item = TestAssetItem.create(new TestCapability());

    CapabilityResult result = context.execute(item, TestCapability.class, new TestCommand());

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getPayload()).isEqualTo("handled");
  }

  @Test
  @DisplayName("등록되지 않은 커맨드는 실패한다")
  void failsWhenCommandNotRegistered() {
    CapabilityExecutionContext context = CapabilityExecutionContext.builder()
        .register(TestCapability.class, new TestHandler())
        .build();
    AssetItem item = TestAssetItem.create(new TestCapability());

    CapabilityResult result = context.execute(item, TestCapability.class, new TestCommand());

    assertThat(result.isFailure()).isTrue();
    assertThat(result.getMessage()).isEqualTo("command not supported");
  }

  @Test
  @DisplayName("자산 특성이 없으면 실패한다")
  void failsWhenCapabilityMissing() {
    CapabilityExecutionContext context = CapabilityExecutionContext.builder()
        .register(TestCapability.class, new TestHandler())
        .build();
    AssetItem item = TestAssetItem.create();

    CapabilityResult result = context.execute(item, TestCapability.class, null);

    assertThat(result.isFailure()).isTrue();
    assertThat(result.getMessage()).isEqualTo("capability not found");
  }

  static final class TestCapability implements AssetCharacteristic {}

  static final class TestCommand implements CapabilityCommand {}

  static final class TestHandler implements CapabilityHandler<TestCapability> {
    @Override
    public CapabilityResult apply(AssetItem item, TestCapability capability) {
      return CapabilityResult.success("handled");
    }
  }

  static final class TestCommandHandler
      implements CommandCapabilityHandler<TestCapability, TestCommand> {
    @Override
    public CapabilityResult apply(AssetItem item, TestCapability capability, TestCommand command) {
      return CapabilityResult.success("handled");
    }
  }

  static final class TestAssetItem extends AssetItem {
    private TestAssetItem(
        AssetItemId id,
        CommunityId communityId,
        UserId contributorId
    ) {
      super(
          id,
          "asset",
          communityId,
          contributorId,
          AssetClassification.cash(),
          Set.of(),
          AssetStatus.ACTIVE
      );
    }

    static TestAssetItem create() {
      return new TestAssetItem(
          AssetItemId.generate(),
          CommunityId.generate(),
          UserId.generate()
      );
    }

    static TestAssetItem create(AssetCharacteristic capability) {
      TestAssetItem item = create();
      item.with(capability);
      return item;
    }
  }
}
