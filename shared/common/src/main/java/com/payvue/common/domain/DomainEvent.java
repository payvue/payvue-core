package com.payvue.common.domain;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
