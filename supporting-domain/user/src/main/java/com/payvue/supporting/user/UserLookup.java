package com.payvue.supporting.user;

interface UserLookup<T> {

  T lookup(UserLookupKey<?> key);
}
