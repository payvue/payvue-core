package com.payvue.supporting.interaction;

import com.payvue.core.community.Participant;


public interface Intent {
    String getCategory();
    Participant initiator();
}
