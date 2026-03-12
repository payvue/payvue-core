package com.payvue.context

import com.payvue.core.relation.CommunityRelation
import com.payvue.core.relation.ConnectionStatus
import com.payvue.core.relation.RelationType
import com.payvue.fixture.RelationFixture
import com.payvue.tags.DomainTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * CommunityRelation 핵심 로직 단위 테스트
 * 
 * 테스트 범위:
 * - associateWith() 팩토리 메서드
 * - RelationType 자동 결정 로직
 * - ConnectionStatus 초기 상태
 * - 기본 불변식 검증
 */
class CommunityRelationUnitTest : BehaviorSpec({
    tags(DomainTest)

    given("개인 커뮤니티 두 개가 주어졌을 때") {
        val (personal1, personal2) = RelationFixture.createPersonalPair()

        `when`("associateWith로 관계를 맺으면") {
            val relation = CommunityRelation.associateWith(personal1, personal2)

            then("FRIENDSHIP 관계가 생성된다") {
                relation.relationType shouldBe RelationType.FRENDSHIP
            }

            then("초기 상태는 PENDING이다") {
                relation.status shouldBe ConnectionStatus.PENDING
                relation.isActive() shouldBe false
            }

            then("RelationId가 자동으로 생성된다") {
                relation.id shouldNotBe null
                relation.id.value shouldNotBe null
            }

            then("from과 to가 올바르게 설정된다") {
                relation.from shouldBe personal1
                relation.to shouldBe personal2
            }
        }
    }

    given("개인 커뮤니티와 단체 커뮤니티가 주어졌을 때") {
        val (personal, organization) = RelationFixture.createPersonalToOrganization()

        `when`("개인이 단체에 관계를 맺으면") {
            val relation = CommunityRelation.associateWith(personal, organization)

            then("SUBSCRIBER 관계가 생성된다") {
                relation.relationType shouldBe RelationType.SUBSCRIBER
            }

            then("초기 상태는 ACTIVE이다") {
                relation.status shouldBe ConnectionStatus.ACTIVE
                relation.isActive() shouldBe true
            }
        }
    }

    given("단체 커뮤니티와 개인 커뮤니티가 주어졌을 때") {
        val (organization, personal) = RelationFixture.createOrganizationToPersonal()

        `when`("단체가 개인에게 관계를 맺으면") {
            val relation = CommunityRelation.associateWith(organization, personal)

            then("PROVIDER 관계가 생성된다") {
                relation.relationType shouldBe RelationType.PROVIDER
            }

            then("초기 상태는 ACTIVE이다") {
                relation.status shouldBe ConnectionStatus.ACTIVE
                relation.isActive() shouldBe true
            }
        }
    }

    given("단체 커뮤니티 두 개가 주어졌을 때") {
        val (org1, org2) = RelationFixture.createOrganizationPair()

        `when`("단체끼리 관계를 맺으면") {
            val relation = CommunityRelation.associateWith(org1, org2)

            then("NEIGHBOR 관계가 생성된다") {
                relation.relationType shouldBe RelationType.NEIGHBOR
            }

            then("초기 상태는 PENDING이다") {
                relation.status shouldBe ConnectionStatus.PENDING
                relation.isActive() shouldBe false
            }
        }
    }

    given("동일한 커뮤니티가 주어졌을 때") {
        val personal = RelationFixture.createPersonal()

        `when`("자기 자신과 관계를 맺으려 하면") {
            val exception = shouldThrow<IllegalArgumentException> {
                CommunityRelation.associateWith(personal, personal)
            }

            then("예외가 발생한다") {
                exception.message shouldBe "동일한 커뮤니티 간에는 관계를 생성할 수 없습니다."
            }
        }
    }

    given("생성된 관계가 주어졌을 때") {
        val (personal1, personal2) = RelationFixture.createPersonalPair()
        val relation = CommunityRelation.associateWith(personal1, personal2)

        `when`("PENDING 상태에서 activate를 호출하면") {
            relation.activate()

            then("상태가 ACTIVE로 변경된다") {
                relation.status shouldBe ConnectionStatus.ACTIVE
                relation.isActive() shouldBe true
            }
        }

        `when`("PENDING 상태에서 terminate를 호출하면") {
            val pendingRelation = CommunityRelation.associateWith(personal1, personal2)
            pendingRelation.terminate()

            then("상태가 TERMINATED로 변경된다") {
                pendingRelation.status shouldBe ConnectionStatus.TERMINATED
                pendingRelation.isActive() shouldBe false
            }
        }
    }

    given("ACTIVE 상태의 관계가 주어졌을 때") {
        val (personal, organization) = RelationFixture.createPersonalToOrganization()
        val relation = CommunityRelation.associateWith(personal, organization)

        `when`("suspend를 호출하면") {
            relation.suspend()

            then("상태가 SUSPENDED로 변경된다") {
                relation.status shouldBe ConnectionStatus.SUSPENDED
                relation.isActive() shouldBe false
            }
        }

        `when`("terminate를 호출하면") {
            val activeRelation = CommunityRelation.associateWith(personal, organization)
            activeRelation.terminate()

            then("상태가 TERMINATED로 변경된다") {
                activeRelation.status shouldBe ConnectionStatus.TERMINATED
                activeRelation.isActive() shouldBe false
            }
        }
    }

    given("SUSPENDED 상태의 관계가 주어졌을 때") {
        val (personal, organization) = RelationFixture.createPersonalToOrganization()
        val relation = CommunityRelation.associateWith(personal, organization)
        relation.suspend()

        `when`("activate를 호출하면") {
            relation.activate()

            then("상태가 ACTIVE로 복구된다") {
                relation.status shouldBe ConnectionStatus.ACTIVE
                relation.isActive() shouldBe true
            }
        }

        `when`("terminate를 호출하면") {
            val suspendedRelation = CommunityRelation.associateWith(personal, organization)
            suspendedRelation.suspend()
            suspendedRelation.terminate()

            then("상태가 TERMINATED로 변경된다") {
                suspendedRelation.status shouldBe ConnectionStatus.TERMINATED
                suspendedRelation.isActive() shouldBe false
            }
        }
    }

    given("TERMINATED 상태의 관계가 주어졌을 때") {
        val (personal1, personal2) = RelationFixture.createPersonalPair()
        val relation = CommunityRelation.associateWith(personal1, personal2)
        relation.terminate()

        `when`("activate를 호출하면") {
            val exception = shouldThrow<IllegalStateException> {
                relation.activate()
            }

            then("예외가 발생한다") {
                exception.shouldBeInstanceOf<IllegalStateException>()
            }
        }

        `when`("suspend를 호출하면") {
            val exception = shouldThrow<IllegalStateException> {
                relation.suspend()
            }

            then("예외가 발생한다") {
                exception.shouldBeInstanceOf<IllegalStateException>()
            }
        }
    }
})
