package com.payvue.supporting.kyc;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.supporting.user.command.UserId;
import lombok.Getter;

import java.time.Instant;

/**
 * UserKyc - 사용자 본인 인증 정보 엔티티
 */
@Getter
public class UserKyc extends AggregateRoot<UserId, UserKyc> implements Identifiable<UserId> {

    private final UserId userId;
    private final String encryptedResidentRegistrationNo;
    private final KycStatus status;
    private final Instant verifiedAt;

    private UserKyc(UserId userId, String encryptedResidentRegistrationNo,
                    KycStatus status, Instant verifiedAt) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (encryptedResidentRegistrationNo == null
            || encryptedResidentRegistrationNo.isBlank()) {
            throw new IllegalArgumentException("주민등록번호는 필수입니다.");
        }
        this.userId = userId;
        this.encryptedResidentRegistrationNo = encryptedResidentRegistrationNo;
        this.status = status != null ? status : KycStatus.PENDING;
        this.verifiedAt = verifiedAt;
    }

    public static UserKyc pending(UserId userId, String encryptedResidentRegistrationNo) {
        return new UserKyc(userId, encryptedResidentRegistrationNo, KycStatus.PENDING, null);
    }

    public UserKyc verify(Instant verifiedAt) {
        return new UserKyc(this.userId, this.encryptedResidentRegistrationNo,
            KycStatus.VERIFIED, verifiedAt != null ? verifiedAt : Instant.now());
    }

    public UserKyc reject() {
        return new UserKyc(this.userId, this.encryptedResidentRegistrationNo,
            KycStatus.REJECTED, this.verifiedAt);
    }

    @Override
    public UserId getId() {
        return userId;
    }
}
