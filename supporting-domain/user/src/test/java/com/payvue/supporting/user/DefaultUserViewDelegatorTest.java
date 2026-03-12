package com.payvue.supporting.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.payvue.supporting.user.command.User;
import com.payvue.supporting.user.command.UserAccountView;
import com.payvue.supporting.user.command.UserId;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultUserViewDelegatorTest {

  @Test
  @DisplayName("조회 결과가 User이면 UserAccountView.from을 통해 변환된다")
  void resolvesUserAccountViewFromUser() {
    User user = newUser();
    DefaultUserViewDelegator delegator = new DefaultUserViewDelegator();
    UserLookup<Object> lookup = key -> user;

    UserAccountView view = delegator.resolve(lookup, newUserIdKey(user.getId()), UserAccountView.class);

    assertThat(view).isNotNull();
    assertThat(view.userId()).isEqualTo(user.getId());
    assertThat(view.email()).isEqualTo("a@b.com");
  }

  @Test
  @DisplayName("조회 결과가 viewType과 같으면 그대로 반환한다")
  void returnsResultWhenAlreadyViewType() {
    UserAccountView view = UserAccountView.from(newUser());
    DefaultUserViewDelegator delegator = new DefaultUserViewDelegator();
    UserLookup<Object> lookup = key -> view;

    UserAccountView resolved = delegator.resolve(lookup, newUserIdKey(view.userId()), UserAccountView.class);

    assertThat(resolved).isSameAs(view);
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

  private UserLookupKey<UserId> newUserIdKey(UserId id) {
    return new UserLookupKey<>() {
      @Override
      public Class<UserId> type() {
        return UserId.class;
      }

      @Override
      public UserId value() {
        return id;
      }
    };
  }
}
