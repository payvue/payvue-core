package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * UserAccount - 사용자 계정 정보 값 객체
 */
@Getter
@EqualsAndHashCode
class Account {

    private final String loginId;
    private final String email;
    private final String phoneNumber;
    private final boolean emailVerified;
    private final boolean phoneVerified;

    private Account(String loginId, String email, String phoneNumber,
                    boolean emailVerified, boolean phoneVerified) {
        validateLoginId(loginId);
        validateEmail(email);
        this.loginId = loginId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
    }

    public static Account of(String loginId, String email, String phoneNumber) {
        return new Account(loginId, email, phoneNumber, false, false);
    }

    public static Account withEmail(String loginId, String email) {
        return new Account(loginId, email, null, false, false);
    }

    public Account verifyEmail() {
        return new Account(this.loginId, this.email, this.phoneNumber, true, this.phoneVerified);
    }

    public Account verifyPhone() {
        return new Account(this.loginId, this.email, this.phoneNumber, this.emailVerified, true);
    }

    public Account changeEmail(String newEmail) {
        return new Account(this.loginId, newEmail, this.phoneNumber, false, this.phoneVerified);
    }

    public Account changeLoginId(String newLoginId) {
        return new Account(newLoginId, this.email, this.phoneNumber, this.emailVerified,
            this.phoneVerified);
    }

    public Account changePhoneNumber(String newPhoneNumber) {
        return new Account(this.loginId, this.email, newPhoneNumber, this.emailVerified, false);
    }

    public boolean isFullyVerified() {
        return emailVerified && phoneVerified;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
    }

    private void validateLoginId(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }
        if (loginId.length() > 50) {
            throw new IllegalArgumentException("아이디는 50자를 초과할 수 없습니다.");
        }
    }
}
