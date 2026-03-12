package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;


@Getter
@EqualsAndHashCode
public class UserId {

    private final UUID value;

    private UserId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        this.value = value;
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
