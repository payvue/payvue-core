package com.payvue.supporting.user.command;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Set<UUID> participantedIn(String email);
}
