package com.payvue.supporting.user;

public interface CommandPolicy {

  boolean validate(UserCommand command);
}
