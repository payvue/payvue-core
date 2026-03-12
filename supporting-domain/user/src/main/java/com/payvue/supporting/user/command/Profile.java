package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

/**
 * UserProfile - 사용자 프로필 값 객체
 */
@Getter
@EqualsAndHashCode
class Profile {

    private final String nickname;
    private final String realName;
    private final LocalDate dateOfBirth;
    private final String profileImageUrl;
    private final String bio;

    private Profile(String nickname, String realName, LocalDate dateOfBirth,
                    String profileImageUrl, String bio) {
        validateNickname(nickname);
        validateRealName(realName);
        this.nickname = nickname;
        this.realName = realName;
        this.dateOfBirth = dateOfBirth;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
    }

    public static Profile of(String nickname, String realName, LocalDate dateOfBirth,
                             String profileImageUrl, String bio) {
        return new Profile(nickname, realName, dateOfBirth, profileImageUrl, bio);
    }

    public static Profile withNickname(String nickname) {
        return new Profile(nickname, null, null, null, null);
    }

    public Profile changeNickname(String newNickname) {
        return new Profile(newNickname, this.realName, this.dateOfBirth,
            this.profileImageUrl, this.bio);
    }

    public Profile changeRealName(String newRealName) {
        return new Profile(this.nickname, newRealName, this.dateOfBirth,
            this.profileImageUrl, this.bio);
    }

    public Profile changeDateOfBirth(LocalDate newDateOfBirth) {
        return new Profile(this.nickname, this.realName, newDateOfBirth,
            this.profileImageUrl, this.bio);
    }

    public Profile changeProfileImage(String newImageUrl) {
        return new Profile(this.nickname, this.realName, this.dateOfBirth,
            newImageUrl, this.bio);
    }

    public Profile changeBio(String newBio) {
        return new Profile(this.nickname, this.realName, this.dateOfBirth,
            this.profileImageUrl, newBio);
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
        if (nickname.length() > 30) {
            throw new IllegalArgumentException("닉네임은 30자를 초과할 수 없습니다.");
        }
    }

    private void validateRealName(String realName) {
        if (realName == null || realName.isBlank()) {
            return;
        }
        if (realName.length() > 50) {
            throw new IllegalArgumentException("이름은 50자를 초과할 수 없습니다.");
        }
    }
}
