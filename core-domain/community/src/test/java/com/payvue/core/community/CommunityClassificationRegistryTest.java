package com.payvue.core.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommunityClassificationRegistryTest {

  @Test
  @DisplayName("카테고리는 대소문자/공백을 정규화하여 조회된다")
  void resolveNormalizesCategory() {
    DefaultCommunityClassificationRegistry registry = new DefaultCommunityClassificationRegistry();
    registry.register(new CommunityRegistration("PERSONAL", TestCommunity.class, command -> null));

    CommunityRegistration resolved = registry.resolve(" personal ");

    assertThat(resolved).isNotNull();
    assertThat(resolved.category()).isEqualTo("PERSONAL");
  }

  @Test
  @DisplayName("등록되지 않은 카테고리는 예외가 발생한다")
  void resolveThrowsWhenMissing() {
    DefaultCommunityClassificationRegistry registry = new DefaultCommunityClassificationRegistry();

    assertThatThrownBy(() -> registry.resolve("MISSING"))
        .isInstanceOf(IllegalArgumentException.class);
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
}
