package com.payvue.generic.alarm;

import java.util.Objects;

public record Trigger(
        String name,
        String role
) {
    public Trigger {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(role, "role is required");
    }
}
