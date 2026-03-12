package com.payvue.core.community;

import com.payvue.supporting.user.command.UserAccountView;

import java.util.Objects;

public record CommunityInitCommand(
    String category,
    UserAccountView view
) {
  public CommunityInitCommand {
    Objects.requireNonNull(category, "category는 필수입니다.");
    Objects.requireNonNull(view, "view는 필수입니다.");
  }
}
