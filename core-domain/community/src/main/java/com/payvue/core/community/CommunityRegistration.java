package com.payvue.core.community;


record CommunityRegistration(
    String category,
    Class<? extends Community> implementClass,
    CommunityInitializer initializer
) {}
