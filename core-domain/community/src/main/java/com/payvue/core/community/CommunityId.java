package com.payvue.core.community;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class CommunityId {

    private final UUID value;

    private CommunityId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("커뮤니티 ID는 필수입니다.");
        }
        this.value = value;
    }

    public static CommunityId of(UUID value) {
        return new CommunityId(value);
    }

    public static CommunityId generate() {
        return new CommunityId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
