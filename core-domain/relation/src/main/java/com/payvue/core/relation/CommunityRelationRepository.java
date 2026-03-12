package com.payvue.core.relation;

import com.payvue.core.community.Community;

import java.util.List;
import java.util.Optional;

public interface CommunityRelationRepository {
    Optional<CommunityRelation> findById(RelationId id);
    void save(CommunityRelation relation);
    void delete(CommunityRelation relation);
    Optional<CommunityRelation> findRelationBetween(Community from, Community to);
    boolean existsRelationBetween(Community from, Community to);
    List<CommunityRelation> findRelationsFrom(Community community, int page, int sz);
    List<CommunityRelation> findRelationsByType(Community community, RelationType type);
    List<CommunityRelation> findPendingRelationsOf(Community community);
    List<CommunityRelation> findActiveRelationsOf(Community community);
}
