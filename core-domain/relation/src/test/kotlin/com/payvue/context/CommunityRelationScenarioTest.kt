package com.payvue.context

import com.payvue.core.relation.CommunityRelation
import com.payvue.core.relation.ConnectionStatus
import com.payvue.core.relation.RelationType
import com.payvue.fixture.RelationFixture
import com.payvue.tags.DomainTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

/**
 * CommunityRelation 시나리오 기반 테스트
 * 
 * 실제 비즈니스 시나리오를 기반으로 한 전체 흐름 검증:
 * - 개인 간 친구 맺기 시나리오
 * - 개인의 단체 구독 시나리오  
 * - 관계 상태 변경 라이프사이클
 */
class CommunityRelationScenarioTest : BehaviorSpec({
    tags(DomainTest)

    given("시나리오: 두 개인이 친구 관계를 맺는다") {
        val alice = RelationFixture.createPersonal("Alice")
        val bob = RelationFixture.createPersonal("Bob")
        val alwaysPass = RelationFixture.alwaysPassPolicy()

        `when`("Alice가 Bob에게 친구 요청을 보낸다") {
            // 1. 정책 검증
            val canAssociate = CommunityRelation.canAssociate(
                alice,
                bob,
                alwaysPass,
                alwaysPass
            )

            then("정책을 통과한다") {
                canAssociate shouldBe true
            }

            // 2. 관계 생성
            val friendship = CommunityRelation.associateWith(alice, bob)

            then("FRIENDSHIP 관계가 생성된다") {
                friendship.relationType shouldBe RelationType.FRENDSHIP
            }

            then("초기 상태는 PENDING (Bob의 수락 대기)이다") {
                friendship.status shouldBe ConnectionStatus.PENDING
                friendship.isActive() shouldBe false
            }

            and("Bob이 친구 요청을 수락하면") {
                friendship.activate()

                then("관계가 ACTIVE 상태가 된다") {
                    friendship.status shouldBe ConnectionStatus.ACTIVE
                    friendship.isActive() shouldBe true
                }
            }

            and("나중에 Alice가 친구 관계를 일시 정지하면") {
                friendship.suspend()

                then("관계가 SUSPENDED 상태가 된다") {
                    friendship.status shouldBe ConnectionStatus.SUSPENDED
                    friendship.isActive() shouldBe false
                }
            }

            and("다시 관계를 복구하면") {
                friendship.activate()

                then("다시 ACTIVE 상태가 된다") {
                    friendship.status shouldBe ConnectionStatus.ACTIVE
                    friendship.isActive() shouldBe true
                }
            }

            and("최종적으로 관계를 완전히 종료하면") {
                friendship.terminate()

                then("TERMINATED 상태가 되어 더 이상 변경할 수 없다") {
                    friendship.status shouldBe ConnectionStatus.TERMINATED
                    friendship.isActive() shouldBe false
                }
            }
        }
    }

    given("시나리오: 개인이 단체 커뮤니티를 구독한다") {
        val user = RelationFixture.createPersonal("사용자")
        val techCommunity = RelationFixture.createOrganization("기술 커뮤니티")
        val alwaysPass = RelationFixture.alwaysPassPolicy()

        `when`("사용자가 기술 커뮤니티 구독을 요청한다") {
            // 1. 정책 검증
            val canSubscribe = CommunityRelation.canAssociate(
                user,
                techCommunity,
                alwaysPass,
                alwaysPass
            )

            then("구독 정책을 통과한다") {
                canSubscribe shouldBe true
            }

            // 2. 구독 관계 생성
            val subscription = CommunityRelation.associateWith(user, techCommunity)

            then("SUBSCRIBER 관계가 생성된다") {
                subscription.relationType shouldBe RelationType.SUBSCRIBER
            }

            then("즉시 ACTIVE 상태가 된다 (별도 승인 불필요)") {
                subscription.status shouldBe ConnectionStatus.ACTIVE
                subscription.isActive() shouldBe true
            }

            and("커뮤니티가 일시적으로 구독을 정지하면") {
                subscription.suspend()

                then("구독이 SUSPENDED 상태가 된다") {
                    subscription.status shouldBe ConnectionStatus.SUSPENDED
                    subscription.isActive() shouldBe false
                }
            }

            and("다시 구독을 활성화하면") {
                subscription.activate()

                then("ACTIVE 상태로 복구된다") {
                    subscription.status shouldBe ConnectionStatus.ACTIVE
                    subscription.isActive() shouldBe true
                }
            }

            and("사용자가 구독을 완전히 취소하면") {
                subscription.terminate()

                then("TERMINATED 상태가 된다") {
                    subscription.status shouldBe ConnectionStatus.TERMINATED
                    subscription.isActive() shouldBe false
                }
            }
        }
    }

    given("시나리오: 단체가 개인에게 콘텐츠를 제공한다") {
        val contentProvider = RelationFixture.createOrganization("콘텐츠 제공자")
        val consumer = RelationFixture.createPersonal("소비자")
        val alwaysPass = RelationFixture.alwaysPassPolicy()

        `when`("제공자가 소비자에게 콘텐츠 제공 관계를 맺는다") {
            // 1. 정책 검증
            val canProvide = CommunityRelation.canAssociate(
                contentProvider,
                consumer,
                alwaysPass,
                alwaysPass
            )

            then("제공 정책을 통과한다") {
                canProvide shouldBe true
            }

            // 2. 제공 관계 생성
            val provision = CommunityRelation.associateWith(contentProvider, consumer)

            then("PROVIDER 관계가 생성된다") {
                provision.relationType shouldBe RelationType.PROVIDER
            }

            then("즉시 ACTIVE 상태가 된다") {
                provision.status shouldBe ConnectionStatus.ACTIVE
                provision.isActive() shouldBe true
            }
        }
    }

    given("시나리오: 두 단체가 협력 관계를 맺는다") {
        val orgA = RelationFixture.createOrganization("조직 A")
        val orgB = RelationFixture.createOrganization("조직 B")
        val alwaysPass = RelationFixture.alwaysPassPolicy()

        `when`("조직 A가 조직 B에게 협력을 제안한다") {
            // 1. 정책 검증
            val canPartner = CommunityRelation.canAssociate(
                orgA,
                orgB,
                alwaysPass,
                alwaysPass
            )

            then("협력 정책을 통과한다") {
                canPartner shouldBe true
            }

            // 2. 협력 관계 생성
            val partnership = CommunityRelation.associateWith(orgA, orgB)

            then("NEIGHBOR 관계가 생성된다") {
                partnership.relationType shouldBe RelationType.NEIGHBOR
            }

            then("초기 상태는 PENDING (조직 B의 승인 대기)이다") {
                partnership.status shouldBe ConnectionStatus.PENDING
                partnership.isActive() shouldBe false
            }

            and("조직 B가 협력을 승인하면") {
                partnership.activate()

                then("관계가 ACTIVE 상태가 된다") {
                    partnership.status shouldBe ConnectionStatus.ACTIVE
                    partnership.isActive() shouldBe true
                }
            }
        }
    }
})
