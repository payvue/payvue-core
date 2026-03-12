package com.payvue.core.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommunityInvariantTest {

  @Test
  @DisplayName("커뮤니티 생성 시 참여자와 결정권자가 포함된다")
  void communityStartsWithAdminParticipant() {
    Community community = new TestCommunity("PERSONAL", new AdminRole());

    assertThat(community.getParticipantCount()).isEqualTo(1);
    UUID adminId = community.getParticipantIds().iterator().next();
    assertThat(community.isParticipant(adminId)).isTrue();
    assertThat(community.isAdmin(adminId)).isTrue();
  }

  @Test
  @DisplayName("결정권자가 없으면 커뮤니티 생성이 실패한다")
  void communityRequiresAdminRole() {
    assertThatThrownBy(() -> new TestCommunity("PERSONAL", new MemberRole()))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("참여자가 없으면 역할 조회 시 예외가 발생한다")
  void getParticipantRoleThrowsWhenMissing() {
    Community community = new TestCommunity("PERSONAL", new AdminRole());
    assertThatThrownBy(() -> community.getParticipantRole(UUID.randomUUID()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  static final class TestCommunity extends Community {
    private final String type;

    TestCommunity(String type, RoleDescriptor adminRole) {
      super("name", "desc", UUID.randomUUID(), adminRole);
      this.type = type;
    }

    @Override
    public String getCommunityType() {
      return type;
    }
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
