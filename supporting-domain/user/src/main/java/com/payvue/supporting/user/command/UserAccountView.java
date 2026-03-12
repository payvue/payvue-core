package com.payvue.supporting.user.command;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record UserAccountView(
    UserId userId,
    String loginId,
    String email,
    String phoneNumber,
    boolean emailVerified,
    String realName,
    String nickname,
    UserStatus status,
    boolean loggedIn,
    Instant lastLoggedIn,
    Map<UUID, Instant> membershipsByCommunity
) {

  public static UserAccountView from(User user) {
    if (user == null) {
      return new UserAccountView(null,null, null, null, false, null, null, null, false, null, Map.of());
    }

    UserId userId = user.getId();

    Account account = user.getAccount();
    Profile profile = user.getProfile();
    String loginId = account == null ? null : account.getLoginId();
    String email = account == null ? null : account.getEmail();
    String phoneNumber = account == null ? null : account.getPhoneNumber();
    boolean emailVerified = account != null && account.isEmailVerified();
    String realName = profile == null ? null : profile.getRealName();
    String nickname = profile == null ? null : profile.getNickname();
    LoginStatus loginStatus = user.getLoginStatus();
    boolean loggedIn = loginStatus != null && loginStatus.isLoggedIn();
    Instant lastLoggedIn = loginStatus == null ? null : loginStatus.getLastLoggedIn();
    Map<UUID, Instant> membershipsByCommunity = new HashMap<>();
    if (user.getMemberships() != null) {
      for (Membership membership : user.getMemberships()) {
        if (membership == null || membership.getCommunityId() == null) {
          continue;
        }
        membershipsByCommunity.put(
            UUID.fromString(membership.getCommunityId()),
            membership.getJoinedAt()
        );
      }
    }
    return new UserAccountView(
        userId,
        loginId,
        email,
        phoneNumber,
        emailVerified,
        realName,
        nickname,
        user.getStatus(),
        loggedIn,
        lastLoggedIn,
        membershipsByCommunity
    );
  }

  public String displayName() {
    return realName != null && !realName.isBlank() ? realName : nickname;
  }

  public Instant joinFor(UUID communityId) {
    if (membershipsByCommunity == null || communityId == null) {
      return null;
    }
    return membershipsByCommunity.get(communityId);
  }
}
