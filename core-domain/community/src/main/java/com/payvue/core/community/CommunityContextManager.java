package com.payvue.core.community;

import com.payvue.supporting.user.command.User;
import java.util.Set;

public interface CommunityContextManager {

  Community get(CategoryQuery query);

  Community init(CommunityInitCommand command);

  Community save(Community community);

  Set<Community> resolve(User user);

  Set<Community> resolve(User user, String category);
}
