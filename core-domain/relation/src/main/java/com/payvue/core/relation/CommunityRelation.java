package com.payvue.core.relation;

import com.payvue.core.community.Community;
import com.payvue.core.community.policy.CommunityPolicy;
import lombok.Getter;


@Getter
public class CommunityRelation {

    private final RelationId id;
    private final Community from;
    private final Community to;
    private final RelationType relationType;
    private ConnectionStatus status;

    private CommunityRelation(RelationId id, Community from,
                              Community to, RelationType relationType,
                              ConnectionStatus initialStatus) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.relationType = relationType;
        this.status = initialStatus;

        validateInvariants();
    }

    public static CommunityRelation associateWith(Community from, Community to) {
        RelationType relationType = RelationType.of(from, to);

        return new CommunityRelation(
            RelationId.generate(),
            from,
            to,
            relationType,
            relationType.initialStatus()
        );
    }

    public static boolean canAssociate(Community from, Community to,
                                       CommunityPolicy fromPolicy,
                                       CommunityPolicy toPolicy) {
        RelationType relationType = RelationType.of(from, to);
        return relationType.allows(from, to, fromPolicy, toPolicy);
    }

    public static CommunityRelation reconstitute(RelationId id,
                                                 Community from,
                                                 Community to,
                                                 RelationType relationType,
                                                 ConnectionStatus status) {
        return new CommunityRelation(id, from, to, relationType, status);
    }

    public void activate() {
        this.status = status.activate();
    }

    public void suspend() {
        this.status = status.suspend();
    }

    public void terminate() {
        this.status = status.terminate();
    }

    public boolean isActive() {
        return status.isActive();
    }

    private void validateSameCommunity(Community from, Community to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("동일한 커뮤니티 간에는 관계를 생성할 수 없습니다.");
        }
    }

    private void validateInvariants() {
        validateSameCommunity(from, to);

        RelationType expectedType = RelationType.of(from, to);
        if (this.relationType != expectedType) {
            throw new IllegalArgumentException(
                String.format(
                    "커뮤니티 타입 조합(%s → %s)에 맞지 않는 관계 유형입니다. 예상: %s, 실제: %s",
                    from.getCommunityType(),
                    to.getCommunityType(),
                    expectedType,
                    this.relationType
                )
            );
        }
    }
}
