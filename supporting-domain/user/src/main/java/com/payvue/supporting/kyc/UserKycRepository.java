package com.payvue.supporting.kyc;

import com.payvue.supporting.user.command.UserId;

import java.util.Optional;

public interface UserKycRepository {

    UserKyc save(UserKyc userKyc);

    Optional<UserKyc> findByUserId(UserId userId);

    void deleteByUserId(UserId userId);
}
