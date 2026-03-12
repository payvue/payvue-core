package com.payvue.generic.alarm;


import java.util.UUID;

public record AlarmId(UUID value) {

    public static AlarmId generate() {
        return new AlarmId(UUID.randomUUID());
    }
}
