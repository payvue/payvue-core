package com.payvue.core.relation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class RelationId {

    private final UUID value;

    private RelationId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("관계 ID는 필수입니다.");
        }
        this.value = value;
    }

    public static RelationId of(UUID value) {
        return new RelationId(value);
    }

    public static RelationId generate() {
        return new RelationId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
