package com.payvue.supporting.user.command;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.supporting.user.FeaturePreference;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class User extends AggregateRoot<UserId, User>
        implements Identifiable<UserId>, UserStatusSupport {

    private final UserId id;
    private Profile profile;
    private Account account;
    private Credentials credentials;
    private UserStatus status;
    private LoginStatus loginStatus;
    private FeaturePreferences featurePreferences;
    private Address address;
    private final List<Membership> memberships;

    private User(UserId id, Profile profile, Account account) {
        this.id = id;
        this.profile = profile;
        this.account = account;
        this.credentials = Credentials.empty();
        this.status = UserStatus.PENDING;
        this.loginStatus = LoginStatus.defaults();
        this.featurePreferences = FeaturePreferences.empty();
        this.address = null;
        this.memberships = new ArrayList<>();
    }

    public static User create(String loginId, String email, String phoneNumber,
                              String nickname, String realName,
                              LocalDate dateOfBirth, String sidoCode,
                              String sigunguCode, String roadAddress,
                              String detailAddress, String postalCode) {
        UserId id = UserId.generate();
        Profile profile = Profile.of(nickname, realName, dateOfBirth, null, null);
        Account account = Account.of(loginId, email, phoneNumber);
        User user = new User(id, profile, account);
        user.address = Address.of(sidoCode, sigunguCode, roadAddress, detailAddress, postalCode);
        return user;
    }

    public static User create(String loginId, String email, String phoneNumber,
                              String nickname, String realName, LocalDate dateOfBirth,
                              String sidoCode, String sigunguCode, String roadAddress,
                              String detailAddress, String postalCode,
                              String encryptedPassword, PasswordPolicy policy) {
        User user = create(loginId, email, phoneNumber, nickname, realName, dateOfBirth,
                sidoCode, sigunguCode, roadAddress, detailAddress, postalCode);
        user.credentials = Credentials.initialize(encryptedPassword, policy);
        return user;
    }

    public static User reconstitute(UserId id, Profile profile, Account account,
                                    UserStatus status, LoginStatus loginStatus,
                                    List<Membership> memberships,
                                    Instant createdAt, Instant updatedAt) {
        User user = new User(id, profile, account);
        user.status = status;
        user.loginStatus = loginStatus != null ? loginStatus : LoginStatus.defaults();
        user.memberships.addAll(memberships);
        return user;
    }

    public static User reconstitute(UserId id, Profile profile, Account account,
                                    Credentials credentials, UserStatus status,
                                    LoginStatus loginStatus, FeaturePreferences featurePreferences,
                                    List<Membership> memberships,
                                    Instant createdAt, Instant updatedAt) {
        User user = reconstitute(id, profile, account, status, loginStatus, memberships, createdAt, updatedAt);
        user.credentials = credentials != null ? credentials : Credentials.empty();
        user.featurePreferences = featurePreferences != null
                ? featurePreferences
                : FeaturePreferences.empty();
        return user;
    }

    public static User reconstitute(UserId id, Profile profile, Account account,
                                    Credentials credentials, UserStatus status,
                                    LoginStatus loginStatus, FeaturePreferences featurePreferences,
                                    Address address, List<Membership> memberships,
                                    Instant createdAt, Instant updatedAt) {
        User user = reconstitute(id, profile, account, credentials, status, loginStatus,
                featurePreferences, memberships, createdAt, updatedAt);
        user.address = address;
        return user;
    }

    public static User reconstitute(UserId id, Profile profile, Account account,
                                    Credentials credentials, UserStatus status,
                                    LoginStatus loginStatus, FeaturePreferences featurePreferences,
                                    String sidoCode, String sigunguCode, String roadAddress,
                                    String detailAddress, String postalCode,
                                    List<Membership> memberships,
                                    Instant createdAt, Instant updatedAt) {
        User user = reconstitute(id, profile, account, credentials, status, loginStatus,
                featurePreferences, memberships, createdAt, updatedAt);
        user.address = Address.of(sidoCode, sigunguCode, roadAddress, detailAddress, postalCode);
        return user;
    }


    public void activate() {
        if (getStatus().isActive()) {
            return;
        }
        setStatus(getStatus().transitionTo(UserStatus.ACTIVE));
    }

    public void suspend() {
        setStatus(getStatus().transitionTo(UserStatus.SUSPENDED));
    }

    public void withdraw() {
        setStatus(getStatus().transitionTo(UserStatus.WITHDRAWN));
    }

    // ==================== 로그인 상태 ====================

    public void markLoggedIn(Instant now) {
        this.loginStatus = this.loginStatus.markLoggedIn(now);
    }

    public void markLoggedOut(Instant now) {
        this.loginStatus = this.loginStatus.markLoggedOut(now);
    }


    public void changeNickname(String newNickname) {
        this.profile = this.profile.changeNickname(newNickname);
    }

    public void changeProfileImage(String newImageUrl) {
        this.profile = this.profile.changeProfileImage(newImageUrl);
    }

    public void changeBio(String newBio) {
        this.profile = this.profile.changeBio(newBio);
    }

    public void changeRealName(String newRealName) {
        this.profile = this.profile.changeRealName(newRealName);
    }

    public void changeDateOfBirth(LocalDate newDateOfBirth) {
        this.profile = this.profile.changeDateOfBirth(newDateOfBirth);
    }

    public void verifyEmail() {
        this.account = this.account.verifyEmail();
    }

    public void verifyPhone() {
        this.account = this.account.verifyPhone();
    }

    public void changeLoginId(String newLoginId) {
        this.account = this.account.changeLoginId(newLoginId);
    }

    // ==================== 보안 관련 메서드 ====================

    public void changePassword(String encryptedPassword, PasswordPolicy policy) {
        this.credentials = this.credentials.changePassword(encryptedPassword, policy);
    }

    // ==================== 설정 관련 메서드 ====================

    public void applyFeaturePreference(FeaturePreference preference) {
        this.featurePreferences = this.featurePreferences.apply(preference);
    }

    public void updateAddress(String sidoCode, String sigunguCode, String roadAddress,
                              String detailAddress, String postalCode) {
        this.address = Address.of(sidoCode, sigunguCode, roadAddress, detailAddress, postalCode);
    }

    public void joinCommunity(String communityId) {
        validateActiveStatus();
        if (isMemberOf(communityId)) {
            throw new IllegalStateException("이미 가입된 커뮤니티입니다.");
        }
        this.memberships.add(Membership.of(communityId));
    }

    public void leaveCommunity(String communityId) {
        Membership membership = findMembership(communityId);
        this.memberships.remove(membership);
    }

    // ==================== 조회 메서드 ====================

    public boolean isMemberOf(String communityId) {
        return memberships.stream()
                .anyMatch(m -> m.getCommunityId().equals(communityId));
    }

    // ==================== Private 메서드 ====================

    private void validateActiveStatus() {
        if (!getStatus().isActive() && !getStatus().isPending()) {
            throw new IllegalStateException("활성 상태의 사용자만 이 작업을 수행할 수 있습니다.");
        }
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    private Membership findMembership(String communityId) {
        return memberships.stream()
                .filter(m -> m.getCommunityId().equals(communityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 커뮤니티입니다."));
    }
}
