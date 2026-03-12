package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

@Getter
@EqualsAndHashCode
class LoginStatus {
    private final Instant lastLoggedIn;
    private final boolean loggedIn;

    private LoginStatus(Instant lastLoggedIn, boolean loggedIn) {
        this.lastLoggedIn = lastLoggedIn;
        this.loggedIn = loggedIn;
    }

    public static LoginStatus defaults() {
        return new LoginStatus(null, false);
    }

    public static LoginStatus of(Instant lastLoggedIn, boolean loggedIn) {
        return new LoginStatus(lastLoggedIn, loggedIn);
    }

    public LoginStatus markLoggedIn(Instant at) {
        return new LoginStatus(at, true);
    }

    public LoginStatus markLoggedOut(Instant at) {
        return new LoginStatus(at, false);
    }
}
