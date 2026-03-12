package com.payvue.core.community;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

class CommunityContextBuilder {

  private final Map<String, CommunityRegistration> registrations = new HashMap<>();
  private CommunityRepository repository;
  private CommunityResolver communityResolver;

  public static CommunityContextBuilder create() {
    return new CommunityContextBuilder();
  }

  public CommunityContextBuilder repository(CommunityRepository repository) {
    this.repository = repository;
    return this;
  }

  public CommunityContextBuilder context(Consumer<ContextSpec> consumer) {
    ContextSpec spec = new ContextSpec();
    consumer.accept(spec);
    CommunityRegistration registration = spec.toRegistration();
    registrations.put(registration.category(), registration);
    return this;
  }

  public CommunityContextBuilder resolver(CommunityResolver communityResolver) {
    this.communityResolver = communityResolver;
    return this;
  }

  public CommunityContextManager build() {
    if (repository == null) {
      throw new IllegalStateException("communityRepository가 설정되지 않았습니다.");
    }
    CommunityClassificationRegistry registry = new DefaultCommunityClassificationRegistry();
    for (CommunityRegistration registration : registrations.values()) {
      registry.register(registration);
    }
    return new DefaultCommunityContextManger(repository, registry, communityResolver);
  }

  public static class ContextSpec {
    private String category;
    private Class<? extends Community> implementClass;
    private CommunityInitializer initializer;

    public ContextSpec category(String category) {
      this.category = category;
      return this;
    }

    public ContextSpec implement(Class<? extends Community> implementClass) {
      this.implementClass = implementClass;
      return this;
    }

    public ContextSpec initialize(CommunityInitializer initializer) {
      this.initializer = initializer;
      return this;
    }

    private CommunityRegistration toRegistration() {
      Objects.requireNonNull(category, "category는 필수입니다.");
      Objects.requireNonNull(implementClass, "implementClass는 필수입니다.");
      Objects.requireNonNull(initializer, "initializer는 필수입니다.");
      return new CommunityRegistration(category, implementClass, initializer);
    }
  }
}
