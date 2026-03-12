package com.payvue.supporting.user;

import com.payvue.supporting.user.command.User;
import com.payvue.supporting.user.command.UserId;

public interface UserContextManager {

  void save(User user, UserCommand command);

  <T> java.util.Optional<T> findById(UserId id, Class<T> viewType);

  <T> java.util.Optional<T> findByEmail(String email, Class<T> viewType);
}
