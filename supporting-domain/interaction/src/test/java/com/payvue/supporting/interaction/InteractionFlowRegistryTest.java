package com.payvue.supporting.interaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.core.community.Participant;
import com.payvue.core.community.RoleDescriptor;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InteractionFlowRegistryTest {

  @Test
  @DisplayName("등록된 플로우는 결정 타입으로 조회된다")
  void resolveReturnsMatchingFlow() {
    InteractionFlowRegistry registry = new InteractionFlowRegistry();
    registry.flow("test-flow")
        .domainService(new TestDomainService(true))
        .handler(new TestHandler(true))
        .build();

    InteractionFlow flow = registry.resolve(new TestDecision(new TestIntent()));

    assertThat(flow.name()).isEqualTo("test-flow");
  }

  @Test
  @DisplayName("매칭되는 플로우가 없으면 예외가 발생한다")
  void resolveThrowsWhenNoFlowMatches() {
    InteractionFlowRegistry registry = new InteractionFlowRegistry();
    registry.flow("test-flow")
        .domainService(new TestDomainService(false))
        .handler(new TestHandler(true))
        .build();

    assertThatThrownBy(() -> registry.resolve(new TestDecision(new TestIntent())))
        .isInstanceOf(UnsupportedOperationException.class);
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

  static final class TestDomainService implements DomainService<TestIntent> {
    private final boolean supports;

    TestDomainService(boolean supports) {
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
  }

  static final class TestHandler implements InteractionHandler<TestIntent> {
    private final boolean supports;

    TestHandler(boolean supports) {
      this.supports = supports;
    }

    @Override
    public boolean supports(Decision<?> decision) {
      return supports;
    }

    @Override
    public void handle(InteractionContext context, Decision<? extends TestIntent> decision) {}
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
