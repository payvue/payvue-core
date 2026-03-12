package com.payvue.core.community;

import com.payvue.supporting.user.command.User;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class DefaultCommunityContextManger implements CommunityContextManager {

  private final CommunityRepository communityRepository;
  private final CommunityClassificationRegistry registry;
  private final CommunityResolver communityResolver;

  public DefaultCommunityContextManger(
      CommunityRepository communityRepository,
      CommunityClassificationRegistry registry,
      CommunityResolver communityResolver
  ) {
    this.communityRepository = Objects.requireNonNull(communityRepository, "communityRepository는 필수입니다.");
    this.registry = Objects.requireNonNull(registry, "registry는 필수입니다.");
    this.communityResolver = communityResolver;
  }

  @Override
  public Community get(CategoryQuery query) {
    CommunityRegistration registration = registry.resolve(query.category());
    if (registration == null) {
      throw new IllegalArgumentException("등록되지 않은 카테고리입니다: " + query.category());
    }
    Community community = communityRepository.findById(query.id())
        .orElseThrow(() -> new IllegalArgumentException("커뮤니티를 찾을 수 없습니다: " + query.id()));
    if (!community.getCommunityType().equalsIgnoreCase(query.category())) {
      throw new IllegalArgumentException("카테고리가 일치하지 않습니다: " + query.category());
    }
    return community;
  }

  @Override
  public Community init(CommunityInitCommand command) {
    CommunityRegistration registration = registry.resolve(command.category());
    if (registration == null) {
      throw new IllegalArgumentException("등록되지 않은 카테고리입니다: " + command.category());
    }
    Community created = registration.initializer().initialize(command);
    Community saved = communityRepository.save(created);
    return saved;
  }

  @Override
  public Community save(Community community) {
    return communityRepository.save(community);
  }

  @Override
  public Set<Community> resolve(User user) {
    Set<CommunityId> ids = communityResolver == null ? Set.of() : communityResolver.resolveIds(user);
    if (ids.isEmpty()) {
      return Set.of();
    }
    return communityRepository.findAllByIds(ids).stream()
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Set<Community> resolve(User user, String category) {
    return resolve(user).stream()
        .filter(community -> category.equalsIgnoreCase(community.getCommunityType()))
        .collect(Collectors.toUnmodifiableSet());
  }
}
