package com.payvue.supporting.user.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMembershipTest {

  @Test
  @DisplayName("비활성 사용자는 가입할 수 없고 중복 가입은 예외가 발생한다")
  void joinRequiresActiveStatusAndPreventsDuplicates() {
    User user = User.create(
        "login",
        "a@b.com",
        "010",
        "nick",
        "real",
        LocalDate.now(),
        "sido",
        "sigungu",
        "road",
        "detail",
        "12345"
    );

    assertThatThrownBy(() -> user.joinCommunity("community-1"))
        .isInstanceOf(IllegalStateException.class);

    user.activate();
    user.joinCommunity("community-1");
    assertThat(user.isMemberOf("community-1")).isTrue();
    assertThatThrownBy(() -> user.joinCommunity("community-1"))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("가입하지 않은 커뮤니티 탈퇴는 예외가 발생한다")
  void leaveFailsWhenNotMember() {
    User user = User.create(
        "login",
        "a@b.com",
        "010",
        "nick",
        "real",
        LocalDate.now(),
        "sido",
        "sigungu",
        "road",
        "detail",
        "12345"
    );
    user.activate();
    assertThatThrownBy(() -> user.leaveCommunity("missing"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
