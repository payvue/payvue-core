package com.payvue.core.relation;

public enum CommunityType {

    PERSONAL,
    ORGANIZATION;

    public static CommunityType from(String communityTypeName) {
        if (communityTypeName == null) {
            throw new IllegalArgumentException("커뮤니티 타입은 필수입니다.");
        }
        
        String normalized = communityTypeName.toUpperCase();
        if ("COOPERATIVE".equals(normalized)) {
            return ORGANIZATION;
        }
        try {
            return CommunityType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "지원하지 않는 커뮤니티 타입입니다: " + communityTypeName
            );
        }
    }
}
