package com.payvue.supporting.user.query;

import com.payvue.supporting.user.command.UserAccountView;
import com.payvue.supporting.user.command.UserId;

import java.util.Optional;

public interface UserQueryRepository {

  Optional<UserAccountView> findById(UserId id);

  Optional<UserAccountView> findByEmail(String email);
}
