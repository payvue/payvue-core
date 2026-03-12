package com.payvue.supporting.user;

import com.payvue.supporting.user.command.User;

import java.lang.reflect.Method;

interface UserViewDelegator {

  <T> T resolve(UserLookup<?> lookup, UserLookupKey<?> key, Class<T> viewType);
}

final class DefaultUserViewDelegator implements UserViewDelegator {

  @Override
  public <T> T resolve(UserLookup<?> lookup, UserLookupKey<?> key, Class<T> viewType) {
    if (lookup == null || key == null || viewType == null) {
      return null;
    }
    Object result = lookup.lookup(key);
    if (result == null) {
      return null;
    }
    if (viewType.isInstance(result)) {
      return viewType.cast(result);
    }
    if (result instanceof User user) {
      T mapped = tryMapFromUser(user, viewType);
      if (mapped != null) {
        return mapped;
      }
    }
    return null;
  }

  private <T> T tryMapFromUser(User user, Class<T> viewType) {
    try {
      Method method = viewType.getMethod("from", User.class);
      Object mapped = method.invoke(null, user);
      if (viewType.isInstance(mapped)) {
        return viewType.cast(mapped);
      }
      return null;
    } catch (ReflectiveOperationException e) {
      return null;
    }
  }
}
