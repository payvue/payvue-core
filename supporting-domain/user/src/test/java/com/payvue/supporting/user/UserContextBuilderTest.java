package com.payvue.supporting.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.supporting.user.command.UserRepository;
import com.payvue.supporting.user.query.UserQueryRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserContextBuilderTest {

  @Test
  @DisplayName("Command repository가 없으면 빌드 시 예외가 발생한다")
  void commandRepositoryIsRequired() {
    assertThatThrownBy(() -> UserContextBuilder.create()
        .query(q -> q.repository(new NoopQueryRepository()))
        .build())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Query repository가 없으면 빌드 시 예외가 발생한다")
  void queryRepositoryIsRequired() {
    assertThatThrownBy(() -> UserContextBuilder.create()
        .command(cmd -> cmd.repository(new NoopUserRepository()))
        .build())
        .isInstanceOf(NullPointerException.class);
  }

  static final class NoopUserRepository implements UserRepository {
    @Override
    public com.payvue.supporting.user.command.User save(com.payvue.supporting.user.command.User user) {
      return user;
    }

    @Override
    public Optional<com.payvue.supporting.user.command.User> findById(
        com.payvue.supporting.user.command.UserId id
    ) {
      return Optional.empty();
    }

    @Override
    public Optional<com.payvue.supporting.user.command.User> findByLoginId(String loginId) {
      return Optional.empty();
    }

    @Override
    public Optional<com.payvue.supporting.user.command.User> findByEmail(String email) {
      return Optional.empty();
    }

    @Override
    public Set<UUID> participantedIn(String email) {
      return Set.of();
    }
  }

  static final class NoopQueryRepository implements UserQueryRepository {
    @Override
    public Optional<com.payvue.supporting.user.command.UserAccountView> findById(
        com.payvue.supporting.user.command.UserId id
    ) {
      return Optional.empty();
    }

    @Override
    public Optional<com.payvue.supporting.user.command.UserAccountView> findByEmail(String email) {
      return Optional.empty();
    }
  }
}
