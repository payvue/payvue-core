package com.payvue.core.relation;

import com.payvue.core.community.Community;
import com.payvue.core.community.policy.CommunityPolicy;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RelationType {

    FRENDSHIP("친구", true) {
        @Override
        public boolean allows(
                Community from,
                Community to,
                CommunityPolicy fromPolicy,
                CommunityPolicy toPolicy
        ) {
            // 친구 관계는 양방향 동의 필요 (from, to 모두 정책 통과)
            boolean fromAllowed = fromPolicy.isSatisfied(from);
            boolean toAllowed = toPolicy != null && toPolicy.isSatisfied(to);
            return fromAllowed && toAllowed;
        }

        @Override
        public ConnectionStatus initialStatus() {
            return ConnectionStatus.ACTIVE;
        }

        @Override
        public boolean matches(CommunityType fromType, CommunityType toType) {
            return fromType == CommunityType.PERSONAL 
                && toType == CommunityType.PERSONAL;
        }
    },

    SUBSCRIBER("구독 관계", false) {
        @Override
        public boolean allows(
                Community from,
                Community to,
                CommunityPolicy fromPolicy,
                CommunityPolicy toPolicy
        ) {
            // 구독 관계는 from만 정책 통과 필요 (to는 무시)
            return fromPolicy.isSatisfied(from);
        }

        @Override
        public ConnectionStatus initialStatus() {
            return ConnectionStatus.ACTIVE;
        }

        @Override
        public boolean matches(CommunityType fromType, CommunityType toType) {
            return fromType == CommunityType.PERSONAL 
                && toType == CommunityType.ORGANIZATION;
        }
    },

    PROVIDER("제공 관계", false) {
        @Override
        public boolean allows(
                Community from,
                Community to,
                CommunityPolicy fromPolicy,
                CommunityPolicy toPolicy
        ) {
            return fromPolicy.isSatisfied(from);
        }

        @Override
        public ConnectionStatus initialStatus() {
            return ConnectionStatus.ACTIVE;
        }

        @Override
        public boolean matches(CommunityType fromType, CommunityType toType) {
            return fromType == CommunityType.ORGANIZATION 
                && toType == CommunityType.PERSONAL;
        }
    },

    NEIGHBOR("이웃 관계", false) {
        @Override
        public boolean allows(
                Community from,
                Community to,
                CommunityPolicy fromPolicy,
                CommunityPolicy toPolicy
        ) {
            // 이웃 관계는 양방향 동의 필요 (from, to 모두 정책 통과)
            boolean fromAllowed = fromPolicy.isSatisfied(from);
            boolean toAllowed = toPolicy != null && toPolicy.isSatisfied(to);
            return fromAllowed && toAllowed;
        }

        @Override
        public ConnectionStatus initialStatus() {
            return ConnectionStatus.PENDING;
        }

        @Override
        public boolean matches(CommunityType fromType, CommunityType toType) {
            return fromType == CommunityType.ORGANIZATION 
                && toType == CommunityType.ORGANIZATION;
        }
    };

    private final String description;
    private final boolean hierarchical;

    RelationType(String description, boolean hierarchical) {
        this.description = description;
        this.hierarchical = hierarchical;
    }

    public abstract boolean allows(
            Community from,
            Community to,
            CommunityPolicy fromPolicy,
            CommunityPolicy toPolicy
    );
    public abstract ConnectionStatus initialStatus();
    public abstract boolean matches(CommunityType fromType, CommunityType toType);

    public static RelationType of(Community from, Community to) {
        CommunityType fromType = CommunityType.from(from.getCommunityType());
        CommunityType toType = CommunityType.from(to.getCommunityType());

        return Arrays.stream(values())
                .filter(type -> type.matches(fromType, toType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(
                                "지원하지 않는 커뮤니티 타입 조합입니다: %s → %s",
                                fromType, toType
                        )
                ));
    }
}
