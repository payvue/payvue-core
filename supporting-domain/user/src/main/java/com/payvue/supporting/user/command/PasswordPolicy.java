package com.payvue.supporting.user.command;

/**
 * PasswordPolicy - 비밀번호 정책
 */
public interface PasswordPolicy {

    void validate(String encryptedPassword, PasswordHistory history);
}
