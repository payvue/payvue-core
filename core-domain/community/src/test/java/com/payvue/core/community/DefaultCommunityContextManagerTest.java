package com.payvue.core.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.supporting.user.command.User;
import com.payvue.supporting.user.command.UserAccountView;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCommunityContextManagerTest {

  @Test
  @DisplayName("등록된 카테고리가 아니면 조회 시 예외가 발생한다")
  void getThrowsWhenCategoryMissing() {
    CommunityContextManager manager = buildManager(Set.of());

    assertThatThrownBy(() -> manager.get(new CategoryQuery("MISSING", CommunityId.generate())))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("카테고리가 일치하지 않으면 조회 시 예외가 발생한다")
  void getThrowsWhenCategoryMismatch() {
    InMemoryCommunityRepository repository = new InMemoryCommunityRepository();
    repository.store = new TestCommunity("OTHER");
    CommunityId id = repository.store.getId();
    CommunityContextManager manager = buildManager(Set.of(repository.store));

    assertThatThrownBy(() -> manager.get(new CategoryQuery("PERSONAL", id)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("커뮤니티 초기화는 등록된 이니셜라이저를 통해 저장된다")
  void initCreatesAndSavesCommunity() {
    InMemoryCommunityRepository repository = new InMemoryCommunityRepository();
    CommunityContextManager manager = buildManager(Set.of(), repository);
    User user = newUser();
    CommunityInitCommand command = new CommunityInitCommand("PERSONAL", UserAccountView.from(user));

    Community created = manager.init(command);

    assertThat(created).isNotNull();
    assertThat(repository.store).isSameAs(created);
  }

  @Test
  @DisplayName("resolve는 resolver가 제공한 id에 해당하는 커뮤니티만 반환한다")
  void resolveFiltersByResolverIds() {
    InMemoryCommunityRepository repository = new InMemoryCommunityRepository();
    TestCommunity community = new TestCommunity("PERSONAL");
    repository.store = community;
    CommunityResolver resolver = user -> Set.of(community.getId());
    CommunityContextManager manager = buildManager(Set.of(community), repository, resolver);

    Set<Community> resolved = manager.resolve(newUser());

    assertThat(resolved).containsExactly(community);
  }

  private CommunityContextManager buildManager(Set<Community> existing) {
    return buildManager(existing, new InMemoryCommunityRepository(), user -> Set.of());
  }

  private CommunityContextManager buildManager(Set<Community> existing, InMemoryCommunityRepository repository) {
    return buildManager(existing, repository, user -> Set.of());
  }

  private CommunityContextManager buildManager(
      Set<Community> existing,
      InMemoryCommunityRepository repository,
      CommunityResolver resolver
  ) {
    repository.all = existing;
    return CommunityContextBuilder.create()
        .repository(repository)
        .context(ctx -> ctx
            .category("PERSONAL")
            .implement(TestCommunity.class)
            .initialize(command -> new TestCommunity("PERSONAL", CommunityId.generate()))
        )
        .resolver(resolver)
        .build();
  }

  private User newUser() {
    return User.create(
        "login",
        "a@b.com",
        "010",
        "nick",
        "real",
        LocalDate.now(),
        "sido",
        "sigungu",
        "road",
        "detail",
        "12345"
    );
  }

  static final class TestCommunity extends Community {
    private final String type;

    TestCommunity(String type) {
      super("name", "desc", UUID.randomUUID(), new TestRole());
      this.type = type;
    }

    @Override
    public String getCommunityType() {
      return type;
    }

  }

  static final class TestRole implements RoleDescriptor {
    @Override
    public String getName() {
      return "ADMIN";
    }

    @Override
    public boolean isAdmin() {
      return true;
    }
  }

  static final class InMemoryCommunityRepository implements CommunityRepository {
    Community store;
    Set<Community> all = Set.of();

    @Override
    public Community save(Community community) {
      store = community;
      return community;
    }

    @Override
    public Optional<Community> findById(CommunityId id) {
      return all.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public List<Community> findAllByIds(Set<CommunityId> ids) {
      return all.stream().filter(c -> ids.contains(c.getId())).toList();
    }

    @Override
    public void delete(Community community) {}

    @Override
    public boolean existsById(CommunityId id) {
      return false;
    }

    @Override
    public boolean existsByName(String name) {
      return false;
    }
  }
}
