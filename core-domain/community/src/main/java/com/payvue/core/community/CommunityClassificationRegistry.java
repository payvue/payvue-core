package com.payvue.core.community;

public interface CommunityClassificationRegistry {

  CommunityClassificationRegistry register(CommunityRegistration registration);

  CommunityRegistration resolve(String category);
}
