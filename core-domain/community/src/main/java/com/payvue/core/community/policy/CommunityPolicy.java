package com.payvue.core.community.policy;

import com.payvue.core.community.Community;

public interface CommunityPolicy {
    boolean isSatisfied(Community community);
}
