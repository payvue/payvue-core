package com.payvue.supporting.user;

import com.payvue.supporting.user.query.UserQueryRepository;

record UserQueryContext(
    UserQueryRepository repository,
    UserLookup<?> lookup
) {}
