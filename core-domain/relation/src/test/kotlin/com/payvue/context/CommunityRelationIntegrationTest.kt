package com.payvue.context

import com.payvue.core.community.Community
import com.payvue.core.community.abs.policy.CommunityPolicy
import com.payvue.core.relation.CommunityRelation
import com.payvue.fixture.RelationFixture
import com.payvue.tags.DomainTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

/**
 * CommunityRelation 통합 테스트
 *
 * 테스트 범위:
 * - canAssociate() 정책 검증 로직
 * - RelationType별 정책 적용 차이
 * - 복합 시나리오 검증
 */
class CommunityRelationIntegrationTest : BehaviorSpec({

    tags(DomainTest)

    given("항상 통과하는 정책이 주어졌을 때") {
        val alwaysPass = RelationFixture.alwaysPassPolicy()

        and("개인 커뮤니티 두 개가 있을 때") {
            val (personal1, personal2) = RelationFixture.createPersonalPair()

            `when`("canAssociate로 관계 가능 여부를 확인하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal1,
                    personal2,
                    alwaysPass,
                    alwaysPass
                )

                then("관계를 맺을 수 있다") {
                    canAssociate shouldBe true
                }
            }
        }

        and("개인이 단체에 구독하려 할 때") {
            val (personal, organization) = RelationFixture.createPersonalToOrganization()

            `when`("canAssociate로 확인하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal,
                    organization,
                    alwaysPass,
                    alwaysPass
                )

                then("구독할 수 있다") {
                    canAssociate shouldBe true
                }
            }
        }

        and("단체가 개인에게 제공하려 할 때") {
            val (organization, personal) = RelationFixture.createOrganizationToPersonal()

            `when`("canAssociate로 확인하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    organization,
                    personal,
                    alwaysPass,
                    alwaysPass
                )

                then("제공할 수 있다") {
                    canAssociate shouldBe true
                }
            }
        }

        and("단체 커뮤니티끼리 이웃 관계를 맺으려 할 때") {
            val (org1, org2) = RelationFixture.createOrganizationPair()

            `when`("canAssociate로 확인하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    org1,
                    org2,
                    alwaysPass,
                    alwaysPass
                )

                then("이웃이 될 수 있다") {
                    canAssociate shouldBe true
                }
            }
        }
    }

    given("실패하는 정책이 주어졌을 때") {
        val alwaysPass = RelationFixture.alwaysPassPolicy()
        val alwaysFail = object : com.payvue.core.community.abs.policy.CommunityPolicy {
            override fun isSatisfied(community: Community): Boolean = false
        }

        and("개인 커뮤니티 두 개가 있을 때 (FRIENDSHIP)") {
            val (personal1, personal2) = RelationFixture.createPersonalPair()

            `when`("from 정책이 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal1,
                    personal2,
                    alwaysFail,
                    alwaysPass
                )

                then("관계를 맺을 수 없다") {
                    canAssociate shouldBe false
                }
            }

            `when`("to 정책이 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal1,
                    personal2,
                    alwaysPass,
                    alwaysFail
                )

                then("관계를 맺을 수 없다 (FRIENDSHIP은 양방향 필수)") {
                    canAssociate shouldBe false
                }
            }

            `when`("양쪽 정책 모두 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal1,
                    personal2,
                    alwaysFail,
                    alwaysFail
                )

                then("관계를 맺을 수 없다") {
                    canAssociate shouldBe false
                }
            }
        }

        and("개인이 단체에 구독하려 할 때 (SUBSCRIBER)") {
            val (personal, organization) = RelationFixture.createPersonalToOrganization()

            `when`("from 정책이 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal,
                    organization,
                    alwaysFail,
                    alwaysPass
                )

                then("구독할 수 없다") {
                    canAssociate shouldBe false
                }
            }

            `when`("to 정책이 실패하면 (toPolicy는 선택적)") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal,
                    organization,
                    alwaysPass,
                    alwaysFail
                )

                then("from 정책만 통과하면 구독할 수 있다") {
                    canAssociate shouldBe true
                }
            }

            `when`("양쪽 정책 모두 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal,
                    organization,
                    alwaysFail,
                    alwaysFail
                )

                then("구독할 수 없다") {
                    canAssociate shouldBe false
                }
            }
        }

        and("단체가 개인에게 제공하려 할 때 (PROVIDER)") {
            val (organization, personal) = RelationFixture.createOrganizationToPersonal()

            `when`("from 정책이 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    organization,
                    personal,
                    alwaysFail,
                    alwaysPass
                )

                then("제공할 수 없다") {
                    canAssociate shouldBe false
                }
            }

            `when`("to 정책이 실패해도 (PROVIDER는 from만 검증)") {
                val canAssociate = CommunityRelation.canAssociate(
                    organization,
                    personal,
                    alwaysPass,
                    alwaysFail
                )

                then("from 정책만 통과하면 제공할 수 있다") {
                    canAssociate shouldBe true
                }
            }

            `when`("양쪽 정책 모두 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    organization,
                    personal,
                    alwaysFail,
                    alwaysFail
                )

                then("제공할 수 없다") {
                    canAssociate shouldBe false
                }
            }
        }

        and("단체 커뮤니티끼리 이웃 관계를 맺으려 할 때 (NEIGHBOR)") {
            val (org1, org2) = RelationFixture.createOrganizationPair()

            `when`("from 정책이 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    org1,
                    org2,
                    alwaysFail,
                    alwaysPass
                )

                then("이웃이 될 수 없다") {
                    canAssociate shouldBe false
                }
            }

            `when`("to 정책이 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    org1,
                    org2,
                    alwaysPass,
                    alwaysFail
                )

                then("이웃이 될 수 없다 (NEIGHBOR는 양방향 필수)") {
                    canAssociate shouldBe false
                }
            }

            `when`("양쪽 정책 모두 실패하면") {
                val canAssociate = CommunityRelation.canAssociate(
                    org1,
                    org2,
                    alwaysFail,
                    alwaysFail
                )

                then("이웃이 될 수 없다") {
                    canAssociate shouldBe false
                }
            }
        }
    }

    given("RelationType별 정책 검증 차이를 확인할 때") {
        val alwaysPass = RelationFixture.alwaysPassPolicy()

        and("FRIENDSHIP은 from, to 모두 정책 통과 필요") {
            val (personal1, personal2) = RelationFixture.createPersonalPair()

            `when`("to 정책이 null이면") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal1,
                    personal2,
                    alwaysPass,
                    null
                )

                then("to 정책이 필수이므로 관계를 맺을 수 없다") {
                    canAssociate shouldBe false
                }
            }
        }

        and("SUBSCRIBER는 from만 정책 통과 필요 (to 무시)") {
            val (personal, organization) = RelationFixture.createPersonalToOrganization()

            `when`("to 정책이 null이어도") {
                val canAssociate = CommunityRelation.canAssociate(
                    personal,
                    organization,
                    alwaysPass,
                    null
                )

                then("from 정책만 통과하면 구독할 수 있다") {
                    canAssociate shouldBe true
                }
            }
        }

        and("PROVIDER는 from만 정책 통과 필요 (to 무시)") {
            val (organization, personal) = RelationFixture.createOrganizationToPersonal()

            `when`("to 정책이 null이어도") {
                val canAssociate = CommunityRelation.canAssociate(
                    organization,
                    personal,
                    alwaysPass,
                    null
                )

                then("from 정책만 통과하면 제공할 수 있다") {
                    canAssociate shouldBe true
                }
            }
        }

        and("NEIGHBOR는 from, to 모두 정책 통과 필요") {
            val (org1, org2) = RelationFixture.createOrganizationPair()

            `when`("to 정책이 null이면") {
                val canAssociate = CommunityRelation.canAssociate(
                    org1,
                    org2,
                    alwaysPass,
                    null
                )

                then("to 정책이 필수이므로 이웃이 될 수 없다") {
                    canAssociate shouldBe false
                }
            }
        }
    }

})
