package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * UserCredentials - 사용자 인증 정보 값 객체
 */
@Getter
@EqualsAndHashCode
class Credentials {

    private final String current;
    private final PasswordHistory history;

    private Credentials(String current, PasswordHistory history) {
        this.current = current;
        this.history = history != null ? history : PasswordHistory.empty();
    }

    public static Credentials empty() {
        return new Credentials(null, PasswordHistory.empty());
    }

    public static Credentials initialize(String encryptedPassword,
                                         PasswordPolicy policy) {
        PasswordHistory history = PasswordHistory.empty();
        policy.validate(encryptedPassword, history);
        history.append(encryptedPassword);
        return new Credentials(encryptedPassword, history);
    }

    public boolean hasPassword() {
        return current != null;
    }

    public Credentials changePassword(String encryptedPassword,
                                      PasswordPolicy policy) {
        policy.validate(encryptedPassword, history);
        PasswordHistory nextHistory = current != null ? history.append(current) : history;
        return new Credentials(encryptedPassword, nextHistory);
    }
}
