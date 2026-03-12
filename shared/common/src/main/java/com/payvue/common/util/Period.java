package com.payvue.common.util;

import lombok.Data;

import java.time.Instant;

@Data
public class Period {
    private final Instant from;
    private final Instant to;
}
