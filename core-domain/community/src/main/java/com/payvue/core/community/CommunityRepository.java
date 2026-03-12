package com.payvue.core.community;

import com.payvue.core.community.Community;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommunityRepository {
    Community save(Community community);
    Optional<Community> findById(CommunityId id);
    List<Community> findAllByIds(Set<CommunityId> ids);
    void delete(Community community);
    boolean existsById(CommunityId id);
    boolean existsByName(String name);
}