package com.payvue.core.community;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class DefaultCommunityClassificationRegistry implements CommunityClassificationRegistry {

  private final Map<String, CommunityRegistration> registrations = new HashMap<>();

  @Override
  public CommunityClassificationRegistry register(CommunityRegistration registration) {
    registrations.put(normalize(registration.category()), registration);
    return this;
  }

  @Override
  public CommunityRegistration resolve(String category) {
    String key = normalize(category);
    if (!registrations.containsKey(key)) {
      throw new IllegalArgumentException("등록되지 않은 카테고리입니다: " + category);
    }
    return registrations.get(key);
  }

  private String normalize(String category) {
    if (category == null) {
      return null;
    }
    return category.trim().toUpperCase(Locale.ROOT);
  }
}
