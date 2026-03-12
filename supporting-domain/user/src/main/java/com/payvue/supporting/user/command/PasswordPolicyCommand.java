package com.payvue.supporting.user.command;

public interface PasswordPolicyCommand {

  String rawPassword();

  String encryptedPassword();

  default PasswordHistory history() {
    return PasswordHistory.empty();
  }
}
