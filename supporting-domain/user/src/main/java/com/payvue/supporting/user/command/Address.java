package com.payvue.supporting.user.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Address - 사용자 주소 값 객체
 */
@Getter
@EqualsAndHashCode
class Address {

    private final String sidoCode;
    private final String sigunguCode;
    private final String roadAddress;
    private final String detailAddress;
    private final String postalCode;

    private Address(String sidoCode, String sigunguCode, String roadAddress,
                    String detailAddress, String postalCode) {
        validateRequired(sidoCode, "시/도");
        validateRequired(sigunguCode, "구/군");
        validateRequired(roadAddress, "도로명 주소");
        validateRequired(postalCode, "우편번호");
        this.sidoCode = sidoCode;
        this.sigunguCode = sigunguCode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.postalCode = postalCode;
    }

    public static Address of(String sidoCode, String sigunguCode, String roadAddress,
                             String detailAddress, String postalCode) {
        return new Address(sidoCode, sigunguCode, roadAddress, detailAddress, postalCode);
    }

    private void validateRequired(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + "는 필수입니다.");
        }
    }
}
