package com.payvue.core.community;

import java.util.Objects;

public record CategoryQuery(
    String category,
    CommunityId id
) {
  public CategoryQuery {
    Objects.requireNonNull(category, "category는 필수입니다.");
    Objects.requireNonNull(id, "id는 필수입니다.");
  }
}
