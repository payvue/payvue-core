package com.payvue.supporting.user.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CredentialsTest {

  @Test
  @DisplayName("비밀번호 초기화와 변경 시 히스토리가 누적된다")
  void initializeAndChangePasswordTrackHistory() {
    PasswordPolicy policy = (encrypted, history) -> {
      if (encrypted == null || encrypted.isBlank()) {
        throw new IllegalArgumentException("encrypted required");
      }
    };
    Credentials credentials = Credentials.initialize("enc-1", policy);
    assertThat(credentials.getCurrent()).isEqualTo("enc-1");
    assertThat(credentials.getHistory().getPrevious()).isEmpty();

    Credentials updated = credentials.changePassword("enc-2", policy);
    assertThat(updated.getCurrent()).isEqualTo("enc-2");
    assertThat(updated.getHistory().getPrevious()).containsExactly("enc-1");
  }
}
