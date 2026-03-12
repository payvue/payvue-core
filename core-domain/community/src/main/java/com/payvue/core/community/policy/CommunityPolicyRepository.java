package com.payvue.core.community.policy;

import com.payvue.core.community.Community;

import java.util.Optional;

public interface CommunityPolicyRepository {

    Optional<CommunityPolicy> findByCommunity(Community community);
    void save(Community community, CommunityPolicy policy);
    void update(Community community, CommunityPolicy policy);
    void delete(Community community);

}
