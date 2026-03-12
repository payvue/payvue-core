package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PasswordHistory - 비밀번호 변경 이력 값 객체
 */
@Getter
@EqualsAndHashCode
class PasswordHistory {

    private final List<String> previous;

    private PasswordHistory(List<String> previous) {
        this.previous = Collections.unmodifiableList(previous);
    }

    public static PasswordHistory empty() {
        return new PasswordHistory(new ArrayList<>());
    }

    public PasswordHistory append(String encryptedPassword) {
        List<String> next = new ArrayList<>(previous);
        next.add(encryptedPassword);
        return new PasswordHistory(next);
    }

    public boolean contains(String encryptedPassword) {
        return previous.contains(encryptedPassword);
    }
}
