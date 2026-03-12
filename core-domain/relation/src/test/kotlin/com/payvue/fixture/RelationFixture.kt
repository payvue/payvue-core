package com.payvue.fixture

import com.payvue.core.community.Organization
import com.payvue.core.community.Personal
import com.payvue.core.community.abs.policy.CommunityPolicy
import java.util.UUID

/**
 * CommunityRelation 테스트를 위한 Fixture
 * 
 * Community 모듈의 CommunityFixture를 활용하여
 * Relation 테스트에 필요한 테스트 데이터를 생성합니다.
 */
object RelationFixture {

    /**
     * 테스트용 UserId 생성
     */
    fun createUserId(): UUID {
        return CommunityFixture.createUserId()
    }

    /**
     * 테스트용 개인 커뮤니티 생성
     * 
     * @param name 커뮤니티 이름 (기본값: "테스트 개인")
     * @param ownerId 소유자 ID (기본값: 랜덤 생성)
     * @return Personal 커뮤니티
     */
    fun createPersonal(
        name: String = "테스트 개인",
        ownerId: UUID = createUserId()
    ): Personal {
        return CommunityFixture.createPersonal(
            name = name,
            description = "${name} 커뮤니티",
            ownerId = ownerId
        )
    }

    /**
     * 테스트용 단체 커뮤니티 생성
     * 
     * @param name 커뮤니티 이름 (기본값: "테스트 단체")
     * @param adminId 관리자 ID (기본값: 랜덤 생성)
     * @return Organization 커뮤니티
     */
    fun createOrganization(
        name: String = "테스트 단체",
        adminId: UUID = createUserId()
    ): Organization {
        return CommunityFixture.createOrganization(
            name = name,
            description = "${name} 커뮤니티",
            adminId = adminId
        )
    }

    /**
     * 항상 통과하는 테스트용 정책
     * 
     * 관계 생성 로직 테스트 시 정책 검증을 무시하고
     * 핵심 로직만 테스트하기 위해 사용합니다.
     * 
     * @return 항상 true를 반환하는 CommunityPolicy
     */
    fun alwaysPassPolicy(): com.payvue.core.community.abs.policy.CommunityPolicy {
        return CommunityFixture.alwaysPassPolicy()
    }

    /**
     * 특정 개인 커뮤니티 쌍 생성
     * 
     * FRIENDSHIP 관계 테스트를 위한 헬퍼 메서드
     * 
     * @return Pair<Personal, Personal>
     */
    fun createPersonalPair(): Pair<Personal, Personal> {
        return Pair(
            createPersonal("개인A"),
            createPersonal("개인B")
        )
    }

    /**
     * 개인 → 단체 커뮤니티 쌍 생성
     * 
     * SUBSCRIBER 관계 테스트를 위한 헬퍼 메서드
     * 
     * @return Pair<Personal, Organization>
     */
    fun createPersonalToOrganization(): Pair<Personal, Organization> {
        return Pair(
            createPersonal("구독자"),
            createOrganization("제공자")
        )
    }

    /**
     * 단체 → 개인 커뮤니티 쌍 생성
     * 
     * PROVIDER 관계 테스트를 위한 헬퍼 메서드
     * 
     * @return Pair<Organization, Personal>
     */
    fun createOrganizationToPersonal(): Pair<Organization, Personal> {
        return Pair(
            createOrganization("제공 조직"),
            createPersonal("수혜자")
        )
    }

    /**
     * 단체 커뮤니티 쌍 생성
     * 
     * NEIGHBOR 관계 테스트를 위한 헬퍼 메서드
     * 
     * @return Pair<Organization, Organization>
     */
    fun createOrganizationPair(): Pair<Organization, Organization> {
        return Pair(
            createOrganization("조직A"),
            createOrganization("조직B")
        )
    }
}
