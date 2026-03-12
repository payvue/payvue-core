package com.payvue.core.community;

import com.payvue.supporting.user.command.User;

import java.util.Set;

public interface CommunityResolver {
    public Set<CommunityId> resolveIds(User user);
}
