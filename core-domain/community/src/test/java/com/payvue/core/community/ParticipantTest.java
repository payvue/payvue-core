package com.payvue.core.community;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.common.status.EngagementState;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParticipantTest {

  @Test
  @DisplayName("Participant 생성 시 기본 상태는 PENDING이다")
  void participantDefaultsToPending() {
    Participant participant = Participant.of(UUID.randomUUID(), new AdminRole());

    assertThat(participant.getEngagementState()).isEqualTo(EngagementState.PENDING);
    assertThat(participant.isAdmin()).isTrue();
  }

  @Test
  @DisplayName("Participant는 지정된 상태로 복원된다")
  void participantReconstitutePreservesState() {
    Participant participant = Participant.reconstitute(
        UUID.randomUUID(),
        UUID.randomUUID(),
        new MemberRole(),
        EngagementState.ACTIVE
    );

    assertThat(participant.getEngagementState()).isEqualTo(EngagementState.ACTIVE);
    assertThat(participant.isAdmin()).isFalse();
  }

  static final class AdminRole implements RoleDescriptor {
    @Override
    public String getName() {
      return "ADMIN";
    }

    @Override
    public boolean isAdmin() {
      return true;
    }
  }

  static final class MemberRole implements RoleDescriptor {
    @Override
    public String getName() {
      return "MEMBER";
    }

    @Override
    public boolean isAdmin() {
      return false;
    }
  }
}
