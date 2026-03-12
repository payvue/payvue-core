package com.payvue.generic.alarm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class Recipient {
    private final RecipientType type;
    private final String value;

    private Recipient(@NonNull RecipientType type, @NonNull String value) {
        this.type = type;
        this.value = value;
    }

    public static Recipient ofUser(String userId) {
        return new Recipient(RecipientType.USER, userId);
    }

    public static Recipient ofCommunity(String communityId) {
        return new Recipient(RecipientType.COMMUNITY, communityId);
    }
}
