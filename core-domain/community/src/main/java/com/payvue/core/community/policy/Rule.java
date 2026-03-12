package com.payvue.core.community.policy;

import com.payvue.core.community.Community;

public interface Rule {
    boolean evaluate(Community community);
}
