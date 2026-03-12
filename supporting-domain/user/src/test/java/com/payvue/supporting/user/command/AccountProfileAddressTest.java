package com.payvue.supporting.user.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountProfileAddressTest {

  @Test
  @DisplayName("계정은 로그인 ID/이메일 검증을 수행하고 기본 검증 상태로 생성된다")
  void accountRequiresValidLoginIdAndEmail() {
    assertThatThrownBy(() -> Account.of(null, "a@b.com", "010"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Account.of("login", "invalid-email", "010"))
        .isInstanceOf(IllegalArgumentException.class);
    Account account = Account.of("login", "a@b.com", "010");
    assertThat(account.getLoginId()).isEqualTo("login");
    assertThat(account.getEmail()).isEqualTo("a@b.com");
    assertThat(account.getPhoneNumber()).isEqualTo("010");
    assertThat(account.isEmailVerified()).isFalse();
    assertThat(account.isPhoneVerified()).isFalse();
  }

  @Test
  @DisplayName("프로필은 닉네임 필수와 길이 제약을 검증하고 필드를 유지한다")
  void profileValidatesNicknameLength() {
    assertThatThrownBy(() -> Profile.of("", "name", LocalDate.now(), null, null))
        .isInstanceOf(IllegalArgumentException.class);
    LocalDate dateOfBirth = LocalDate.now();
    Profile profile = Profile.of("nick", "name", dateOfBirth, null, null);
    assertThat(profile.getNickname()).isEqualTo("nick");
    assertThat(profile.getRealName()).isEqualTo("name");
    assertThat(profile.getDateOfBirth()).isEqualTo(dateOfBirth);
    assertThat(profile.getProfileImageUrl()).isNull();
    assertThat(profile.getBio()).isNull();
  }

  @Test
  @DisplayName("주소는 필수 필드를 검증하고 생성된 값을 보존한다")
  void addressRequiresMandatoryFields() {
    assertThatThrownBy(() -> Address.of(null, "sigungu", "road", "detail", "12345"))
        .isInstanceOf(IllegalArgumentException.class);
    Address address = Address.of("sido", "sigungu", "road", "detail", "12345");
    assertThat(address.getSidoCode()).isEqualTo("sido");
    assertThat(address.getSigunguCode()).isEqualTo("sigungu");
    assertThat(address.getRoadAddress()).isEqualTo("road");
    assertThat(address.getDetailAddress()).isEqualTo("detail");
    assertThat(address.getPostalCode()).isEqualTo("12345");
  }
}
