package com.payvue.core.transaction.command;

import com.payvue.core.community.Community;
import com.payvue.core.community.Participant;
import com.payvue.core.hold.Holdings;
import com.payvue.supporting.user.command.UserId;

public record Actor(
    Community community,
    Holdings holdings,
    UserId actorId,
    BindingKey binderKey
) {

    public Participant act() {
        return Participant.of(actorId.getValue(), community.getParticipantRole(actorId.getValue()));
    }
}
