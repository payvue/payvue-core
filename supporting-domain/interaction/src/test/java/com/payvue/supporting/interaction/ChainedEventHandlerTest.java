package com.payvue.supporting.interaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.core.community.Participant;
import com.payvue.core.community.RoleDescriptor;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChainedEventHandlerTest {

  @Test
  @DisplayName("FIRST_MATCH는 첫 매칭 핸들러만 실행한다")
  void firstMatchStopsAfterFirst() {
    AtomicInteger firstCalls = new AtomicInteger();
    AtomicInteger secondCalls = new AtomicInteger();

    ChainedEventHandler<TestIntent> handler = ChainedEventHandler.<TestIntent>builder()
        .add(new CountingHandler(true, firstCalls))
        .add(new CountingHandler(true, secondCalls))
        .firstMatch()
        .build();

    handler.handle(new StubContext(true), new TestDecision(new TestIntent()));

    assertThat(firstCalls.get()).isEqualTo(1);
    assertThat(secondCalls.get()).isZero();
  }

  @Test
  @DisplayName("ALL_MATCHES는 매칭되는 모든 핸들러를 실행한다")
  void allMatchesExecutesAllMatchingHandlers() {
    AtomicInteger firstCalls = new AtomicInteger();
    AtomicInteger secondCalls = new AtomicInteger();

    ChainedEventHandler<TestIntent> handler = ChainedEventHandler.<TestIntent>builder()
        .add(new CountingHandler(true, firstCalls))
        .add(new CountingHandler(true, secondCalls))
        .allMatches()
        .build();

    handler.handle(new StubContext(true), new TestDecision(new TestIntent()));

    assertThat(firstCalls.get()).isEqualTo(1);
    assertThat(secondCalls.get()).isEqualTo(1);
  }

  @Test
  @DisplayName("supports는 매칭되는 핸들러가 있으면 true를 반환한다")
  void supportsReturnsTrueWhenAnyHandlerMatches() {
    ChainedEventHandler<TestIntent> handler = ChainedEventHandler.<TestIntent>builder()
        .add(new CountingHandler(false, new AtomicInteger()))
        .add(new CountingHandler(true, new AtomicInteger()))
        .build();

    boolean supported = handler.supports(new TestDecision(new TestIntent()));

    assertThat(supported).isTrue();
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

  static final class CountingHandler implements InteractionHandler<TestIntent> {
    private final boolean supports;
    private final AtomicInteger calls;

    CountingHandler(boolean supports, AtomicInteger calls) {
      this.supports = supports;
      this.calls = calls;
    }

    @Override
    public boolean supports(Decision<?> decision) {
      return supports;
    }

    @Override
    public void handle(InteractionContext context, Decision<? extends TestIntent> decision) {
      calls.incrementAndGet();
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
