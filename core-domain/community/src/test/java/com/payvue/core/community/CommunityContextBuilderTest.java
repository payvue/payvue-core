package com.payvue.core.community;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommunityContextBuilderTest {

  @Test
  @DisplayName("CommunityRepository가 없으면 빌드 시 예외가 발생한다")
  void repositoryIsRequired() {
    assertThatThrownBy(() -> CommunityContextBuilder.create()
        .context(ctx -> ctx
            .category("PERSONAL")
            .implement(TestCommunity.class)
            .initialize(command -> new TestCommunity())
        )
        .build())
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("ContextSpec 필수 값이 없으면 예외가 발생한다")
  void contextSpecRequiresFields() {
    assertThatThrownBy(() -> CommunityContextBuilder.create()
        .repository(new InMemoryCommunityRepository())
        .context(ctx -> ctx
            .category("PERSONAL")
        )
        .build())
        .isInstanceOf(NullPointerException.class);
  }

  static final class TestCommunity extends Community {
    TestCommunity() {
      super("name", "desc", java.util.UUID.randomUUID(), new TestRole());
    }

    @Override
    public String getCommunityType() {
      return "PERSONAL";
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
    @Override
    public Community save(Community community) {
      return community;
    }

    @Override
    public java.util.Optional<Community> findById(CommunityId id) {
      return java.util.Optional.empty();
    }

    @Override
    public java.util.List<Community> findAllByIds(java.util.Set<CommunityId> ids) {
      return java.util.List.of();
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
