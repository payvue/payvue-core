package com.payvue.supporting.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.supporting.user.FeaturePreference;
import com.payvue.supporting.user.command.FeatureKey;
import com.payvue.supporting.user.command.FeaturePreferences;
import com.payvue.supporting.user.command.User;
import com.payvue.supporting.user.command.UserAccountView;
import com.payvue.supporting.user.command.UserId;
import com.payvue.supporting.user.command.UserRepository;
import com.payvue.supporting.user.query.UserQueryRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserContextManagerTest {

  @Test
  @DisplayName("저장 시 정책이 통과하면 기본 기능 설정이 적용된다")
  void saveAppliesPolicyAndFeatureDefaults() {
    InMemoryUserRepository repository = new InMemoryUserRepository();
    UserContextManager manager = UserContextBuilder.create()
        .command(cmd -> cmd
            .repository(repository)
            .context("REGISTER")
            .policy(command -> true)
            .features(
                FeatureDefaultsRegistry.builder()
                    .register(new TestPreference())
                    .build()
            )
        )
        .query(q -> q
            .repository(new InMemoryUserQueryRepository())
        )
        .build();

    User user = newUser();
    manager.save(user, () -> "REGISTER");

    assertThat(repository.lastSaved).isSameAs(user);
    FeaturePreferences preferences = user.getFeaturePreferences();
    assertThat(preferences.getValues()).containsKey(TestPreference.KEY);
  }

  @Test
  @DisplayName("정책이 거부되면 저장이 수행되지 않는다")
  void saveSkipsWhenPolicyRejects() {
    InMemoryUserRepository repository = new InMemoryUserRepository();
    UserContextManager manager = UserContextBuilder.create()
        .command(cmd -> cmd
            .repository(repository)
            .context("REGISTER")
            .policy(command -> false)
        )
        .query(q -> q
            .repository(new InMemoryUserQueryRepository())
        )
        .build();

    User user = newUser();
    manager.save(user, () -> "REGISTER");

    assertThat(repository.lastSaved).isNull();
  }

  @Test
  @DisplayName("조회 결과가 User일 때 UserAccountView로 매핑된다")
  void lookupResolvesUserAccountViewFromUser() {
    User user = newUser();
    UserId userId = user.getId();
    InMemoryUserRepository repository = new InMemoryUserRepository();
    repository.store = user;

    UserContextManager manager = UserContextBuilder.create()
        .command(cmd -> cmd
            .repository(repository)
            .lookup(new UserLookupRegistry()
                .register(UserId.class, key -> repository.store)
                .register(String.class, key -> repository.store)
                ::lookup)
        )
        .query(q -> q
            .repository(new InMemoryUserQueryRepository())
            .lookup(new UserLookupRegistry()
                .register(UserId.class, key -> repository.store)
                .register(String.class, key -> repository.store)
                ::lookup)
        )
        .build();

    Optional<UserAccountView> view = manager.findById(userId, UserAccountView.class);
    assertThat(view).isPresent();
    assertThat(view.get().userId()).isEqualTo(userId);
  }

  @Test
  @DisplayName("이메일이 공백이면 조회 결과는 비어 있다")
  void findByEmailReturnsEmptyForBlankEmail() {
    UserContextManager manager = UserContextBuilder.create()
        .command(cmd -> cmd.repository(new InMemoryUserRepository()))
        .query(q -> q.repository(new InMemoryUserQueryRepository()))
        .build();

    assertThat(manager.findByEmail(" ", UserAccountView.class)).isEmpty();
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

  static final class InMemoryUserRepository implements UserRepository {
    User store;
    User lastSaved;

    @Override
    public User save(User user) {
      store = user;
      lastSaved = user;
      return user;
    }

    @Override
    public Optional<User> findById(UserId id) {
      return Optional.ofNullable(store);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
      return Optional.ofNullable(store);
    }

    @Override
    public Optional<User> findByEmail(String email) {
      return Optional.ofNullable(store);
    }

    @Override
    public Set<UUID> participantedIn(String email) {
      return Set.of();
    }
  }

  static final class InMemoryUserQueryRepository implements UserQueryRepository {
    @Override
    public Optional<UserAccountView> findById(UserId id) {
      return Optional.empty();
    }

    @Override
    public Optional<UserAccountView> findByEmail(String email) {
      return Optional.empty();
    }
  }

  static final class TestPreference implements FeaturePreference {
    static final FeatureKey KEY = FeatureKey.of("TEST_PREF");

    @Override
    public FeatureKey key() {
      return KEY;
    }
  }
}
