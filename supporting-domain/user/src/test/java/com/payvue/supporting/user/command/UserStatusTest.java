package com.payvue.supporting.user.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserStatusTest {

  @Test
  @DisplayName("PENDING은 ACTIVE로만 전이 가능하다")
  void pendingCanTransitionToActiveOnly() {
    assertThat(UserStatus.PENDING.canTransitionTo(UserStatus.ACTIVE)).isTrue();
    assertThat(UserStatus.PENDING.canTransitionTo(UserStatus.SUSPENDED)).isFalse();
    assertThat(UserStatus.PENDING.canTransitionTo(UserStatus.WITHDRAWN)).isFalse();
  }

  @Test
  @DisplayName("ACTIVE는 SUSPENDED 또는 WITHDRAWN으로 전이 가능하다")
  void activeCanTransitionToSuspendedOrWithdrawn() {
    assertThat(UserStatus.ACTIVE.canTransitionTo(UserStatus.SUSPENDED)).isTrue();
    assertThat(UserStatus.ACTIVE.canTransitionTo(UserStatus.WITHDRAWN)).isTrue();
    assertThat(UserStatus.ACTIVE.canTransitionTo(UserStatus.PENDING)).isFalse();
  }

  @Test
  @DisplayName("WITHDRAWN은 어떤 상태로도 전이할 수 없다")
  void withdrawnCannotTransition() {
    assertThat(UserStatus.WITHDRAWN.canTransitionTo(UserStatus.ACTIVE)).isFalse();
    assertThatThrownBy(() -> UserStatus.WITHDRAWN.transitionTo(UserStatus.ACTIVE))
        .isInstanceOf(UserStatus.IllegalStateTransitionException.class);
  }

  @Test
  @DisplayName("허용된 상태 전이는 성공하고 허용되지 않은 전이는 예외를 던진다")
  void transitionEnforcesStateRules() {
    assertThat(UserStatus.PENDING.transitionTo(UserStatus.ACTIVE)).isEqualTo(UserStatus.ACTIVE);
    assertThat(UserStatus.ACTIVE.transitionTo(UserStatus.SUSPENDED)).isEqualTo(UserStatus.SUSPENDED);
    assertThat(UserStatus.SUSPENDED.transitionTo(UserStatus.ACTIVE)).isEqualTo(UserStatus.ACTIVE);
    assertThatThrownBy(() -> UserStatus.PENDING.transitionTo(UserStatus.WITHDRAWN))
        .isInstanceOf(UserStatus.IllegalStateTransitionException.class);
  }
}
