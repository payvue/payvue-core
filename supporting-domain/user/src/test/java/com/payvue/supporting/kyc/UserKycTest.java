package com.payvue.supporting.kyc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.supporting.user.command.UserId;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserKycTest {

  @Test
  @DisplayName("KYC 대기 상태는 기본 상태가 PENDING이다")
  void pendingCreatesPendingStatus() {
    UserKyc kyc = UserKyc.pending(UserId.of(UUID.randomUUID()), "enc-rrn");
    assertThat(kyc.getStatus()).isEqualTo(KycStatus.PENDING);
    assertThat(kyc.getVerifiedAt()).isNull();
  }

  @Test
  @DisplayName("KYC 검증은 VERIFIED 상태와 검증 시각을 설정한다")
  void verifySetsStatusAndTimestamp() {
    Instant now = Instant.now();
    UserKyc pending = UserKyc.pending(UserId.of(UUID.randomUUID()), "enc-rrn");
    UserKyc verified = pending.verify(now);

    assertThat(verified.getStatus()).isEqualTo(KycStatus.VERIFIED);
    assertThat(verified.getVerifiedAt()).isEqualTo(now);
  }

  @Test
  @DisplayName("KYC 거절은 REJECTED 상태로 전이된다")
  void rejectSetsStatusRejected() {
    UserKyc pending = UserKyc.pending(UserId.of(UUID.randomUUID()), "enc-rrn");
    UserKyc rejected = pending.reject();

    assertThat(rejected.getStatus()).isEqualTo(KycStatus.REJECTED);
  }

  @Test
  @DisplayName("KYC 생성 시 사용자 ID와 주민번호는 필수이다")
  void pendingRequiresUserIdAndResidentRegistrationNo() {
    assertThatThrownBy(() -> UserKyc.pending(null, "enc-rrn"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> UserKyc.pending(UserId.of(UUID.randomUUID()), " "))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
