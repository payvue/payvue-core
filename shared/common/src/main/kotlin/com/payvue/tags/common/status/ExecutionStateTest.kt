package com.payvue.context.interaction.state

import com.payvue.common.status.ExecutionState
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

/**
 * ExecutionStateTest
 * 
 * Layer 3: 단위 테스트
 * - ExecutionState enum의 상태 전이 로직 검증
 */
class ExecutionStateTest : BehaviorSpec({
    
    Given("PENDING_APPROVAL 상태일 때") {
        val state = ExecutionState.PENDING_APPROVAL
        
        When("APPROVED로 전이하면") {
            val newState = state.transitionTo(ExecutionState.APPROVED)
            
            Then("성공한다") {
                newState shouldBe ExecutionState.APPROVED
            }
        }
        
        When("REJECTED로 전이하면") {
            val newState = state.transitionTo(ExecutionState.REJECTED)
            
            Then("성공한다") {
                newState shouldBe ExecutionState.REJECTED
            }
        }
        
        When("COMPLETED로 직접 전이하면") {
            Then("예외가 발생한다") {
                shouldThrow<ExecutionState.IllegalStateTransitionException> {
                    state.transitionTo(ExecutionState.COMPLETED)
                }
            }
        }
    }
    
    Given("APPROVED 상태일 때") {
        val state = ExecutionState.APPROVED
        
        When("COMPLETED로 전이하면") {
            val newState = state.transitionTo(ExecutionState.COMPLETED)
            
            Then("성공한다") {
                newState shouldBe ExecutionState.COMPLETED
            }
        }
        
        When("FAILED로 전이하면") {
            val newState = state.transitionTo(ExecutionState.FAILED)
            
            Then("성공한다") {
                newState shouldBe ExecutionState.FAILED
            }
        }
        
        When("PENDING_APPROVAL로 되돌리려 하면") {
            Then("예외가 발생한다") {
                shouldThrow<ExecutionState.IllegalStateTransitionException> {
                    state.transitionTo(ExecutionState.PENDING_APPROVAL)
                }
            }
        }
    }
    
    Given("COMPLETED 상태일 때") {
        val state = ExecutionState.COMPLETED
        
        When("어떤 상태로도 전이하려 하면") {
            Then("예외가 발생한다") {
                shouldThrow<ExecutionState.IllegalStateTransitionException> {
                    state.transitionTo(ExecutionState.APPROVED)
                }
            }
        }
        
        Then("isFinal()이 true이다") {
            state.isFinal() shouldBe true
        }
    }
    
    Given("REJECTED 상태일 때") {
        val state = ExecutionState.REJECTED
        
        Then("최종 상태이다") {
            state.isFinal() shouldBe true
        }
        
        When("전이를 시도하면") {
            Then("예외가 발생한다") {
                shouldThrow<ExecutionState.IllegalStateTransitionException> {
                    state.transitionTo(ExecutionState.PENDING_APPROVAL)
                }
            }
        }
    }
})
