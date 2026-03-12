package com.payvue.supporting.interaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.common.status.ExecutionState;
import com.payvue.core.community.Participant;
import com.payvue.core.community.RoleDescriptor;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InteractionFlowTest {

  @Test
  @DisplayName("컨텍스트 검증이 실패하면 Interaction은 실패 상태가 된다")
  void executeFailsWhenContextValidationFails() {
    InteractionFlow flow = new InteractionFlow(
        "test-flow",
        new RecordingDomainService(true),
        new RecordingHandler(true)
    );

    Interaction interaction = flow.execute(new StubContext(false), new TestDecision(new TestIntent()));

    assertThat(interaction.getStatus()).isEqualTo(ExecutionState.FAILED);
    assertThat(interaction.isFailed()).isTrue();
  }

  @Test
  @DisplayName("핸들러가 결정 타입을 지원하지 않으면 Interaction은 실패한다")
  void executeFailsWhenHandlerDoesNotSupport() {
    InteractionFlow flow = new InteractionFlow(
        "test-flow",
        new RecordingDomainService(true),
        new RecordingHandler(false)
    );

    Interaction interaction = flow.execute(new StubContext(true), new TestDecision(new TestIntent()));

    assertThat(interaction.getStatus()).isEqualTo(ExecutionState.FAILED);
    assertThat(interaction.isFailed()).isTrue();
  }

  @Test
  @DisplayName("승인 대기 상태로 반환되면 Interaction은 완료로 전이된다")
  void executeCompletesWhenPendingApprovalReturned() {
    RecordingDomainService domainService = new RecordingDomainService(true);
    RecordingHandler handler = new RecordingHandler(true);
    InteractionFlow flow = new InteractionFlow("test-flow", domainService, handler);

    Interaction interaction = flow.execute(new StubContext(true), new TestDecision(new TestIntent()));

    assertThat(handler.called()).isTrue();
    assertThat(interaction.getStatus()).isEqualTo(ExecutionState.COMPLETED);
    assertThat(interaction.isCompleted()).isTrue();
  }

  static final class TestIntent implements Intent {
    private final Participant initiator =
        Participant.of(UUID.randomUUID(), new TestRole("ADMIN", true));

    @Override
    public String getCategory() {
      return "TEST";
    }

    @Override
    public Participant initiator() {
      return initiator;
    }
  }

  static final class TestDecision extends Decision<TestIntent> {
    TestDecision(TestIntent intent) {
      super(intent);
    }
  }

  static final class StubContext implements InteractionContext {
    private final boolean valid;

    StubContext(boolean valid) {
      this.valid = valid;
    }

    @Override
    public Interaction execute(Decision<?> decision) {
      return Interaction.pending(decision);
    }

    @Override
    public boolean supports(Decision<?> decision, DomainService service) {
      return service.supports(decision);
    }

    @Override
    public boolean validate(Decision<?> decision) {
      return valid;
    }
  }

  static final class RecordingDomainService implements DomainService<TestIntent> {
    private final boolean supports;

    RecordingDomainService(boolean supports) {
      this.supports = supports;
    }

    @Override
    public boolean supports(Decision<?> decision) {
      return supports;
    }

    @Override
    public Interaction execute(Decision<TestIntent> decision) {
      return Interaction.pending(decision);
    }

    @Override
    public Interaction execute(
        InteractionContext context,
        Decision<TestIntent> decision,
        InteractionHandler<TestIntent> handler
    ) {
      handler.handle(context, decision);
      return Interaction.pending(decision);
    }
  }

  static final class RecordingHandler implements InteractionHandler<TestIntent> {
    private final boolean supports;
    private final AtomicBoolean called = new AtomicBoolean(false);

    RecordingHandler(boolean supports) {
      this.supports = supports;
    }

    boolean called() {
      return called.get();
    }

    @Override
    public boolean supports(Decision<?> decision) {
      return supports;
    }

    @Override
    public void handle(InteractionContext context, Decision<? extends TestIntent> decision) {
      called.set(true);
    }
  }

  static final class TestRole implements RoleDescriptor {
    private final String name;
    private final boolean admin;

    TestRole(String name, boolean admin) {
      this.name = name;
      this.admin = admin;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isAdmin() {
      return admin;
    }
  }
}
