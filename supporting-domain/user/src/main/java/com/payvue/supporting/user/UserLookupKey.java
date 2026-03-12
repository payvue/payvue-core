package com.payvue.supporting.user;

interface UserLookupKey<T> {

  Class<T> type();

  T value();
}
