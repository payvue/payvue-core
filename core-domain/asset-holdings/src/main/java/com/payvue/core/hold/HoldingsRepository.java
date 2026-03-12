package com.payvue.core.hold;


import com.payvue.core.asset.AssetItem;
import com.payvue.core.community.Community;

import java.util.Set;

public interface HoldingsRepository {
  boolean existsById(HoldingsId id);
  Holdings findByCommunity(Community community);
  Set<Holdings> findByCommunities(Set<Community> communities);
  Set<AssetItem> findByCommunityBySepcification(Community community, HoldingSpecification specification);
  Set<AssetItem> findByCommunitiesBySepcfication(Set<Community> community, HoldingSpecification specification);
  void save(Holdings holdings);
}
