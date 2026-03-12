package com.payvue.core.community;


interface CommunityInitializer {

  String category();

  Community initialize(CommunityInitCommand command);
}
