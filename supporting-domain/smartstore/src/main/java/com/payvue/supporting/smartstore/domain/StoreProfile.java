package com.payvue.supporting.smartstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * StoreProfile - 스토어 프로필 값 객체
 */
@Getter
@EqualsAndHashCode
public class StoreProfile {

    private final String name;
    private final String description;
    private final String logoUrl;
    private final String contactEmail;
    private final String contactPhone;

    private StoreProfile(String name, String description, String logoUrl,
                        String contactEmail, String contactPhone) {
        validateName(name);
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
    }

    public static StoreProfile of(String name, String description) {
        return new StoreProfile(name, description, null, null, null);
    }

    public static StoreProfile create(String name, String description, String logoUrl,
                                      String contactEmail, String contactPhone) {
        return new StoreProfile(name, description, logoUrl, contactEmail, contactPhone);
    }

    public StoreProfile changeName(String newName) {
        return new StoreProfile(newName, this.description, this.logoUrl, 
                               this.contactEmail, this.contactPhone);
    }

    public StoreProfile changeDescription(String newDescription) {
        return new StoreProfile(this.name, newDescription, this.logoUrl,
                               this.contactEmail, this.contactPhone);
    }

    public StoreProfile changeLogo(String newLogoUrl) {
        return new StoreProfile(this.name, this.description, newLogoUrl,
                               this.contactEmail, this.contactPhone);
    }

    public StoreProfile changeContact(String email, String phone) {
        return new StoreProfile(this.name, this.description, this.logoUrl, email, phone);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("스토어 이름은 필수입니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("스토어 이름은 100자를 초과할 수 없습니다.");
        }
    }
}
