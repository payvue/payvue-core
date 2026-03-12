package com.payvue.core.community.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.payvue.core.community.Community;
import com.payvue.core.community.RoleDescriptor;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommunityPolicyTest {

  @Test
  @DisplayName("SingleRulePolicy는 단일 규칙 평가 결과를 반환한다")
  void singleRulePolicyEvaluatesRule() {
    SingleRulePolicy policy = SingleRulePolicy.of(new FixedRule(true));
    assertThat(policy.isSatisfied(new TestCommunity())).isTrue();
  }

  @Test
  @DisplayName("ChainedRulePolicy는 체인 중 하나라도 실패하면 false를 반환한다")
  void chainedRulePolicyEvaluatesChain() {
    ChainableRule rule = new FixedChainRule(true).append(new FixedChainRule(false));
    ChainedRulePolicy policy = ChainedRulePolicy.of(rule);
    assertThat(policy.isSatisfied(new TestCommunity())).isFalse();
  }

  @Test
  @DisplayName("ChainedRulePolicy.Builder는 규칙이 없으면 예외가 발생한다")
  void chainedRulePolicyRequiresRules() {
    assertThatThrownBy(() -> ChainedRulePolicy.builder().build())
        .isInstanceOf(IllegalStateException.class);
  }

  static final class FixedRule implements Rule {
    private final boolean result;

    FixedRule(boolean result) {
      this.result = result;
    }

    @Override
    public boolean evaluate(Community community) {
      return result;
    }
  }

  static final class FixedChainRule extends ChainableRule {
    private final boolean result;

    FixedChainRule(boolean result) {
      this.result = result;
    }

    @Override
    public boolean evaluate(Community community) {
      return result;
    }
  }

  static final class TestCommunity extends Community {
    TestCommunity() {
      super("name", "desc", UUID.randomUUID(), new TestRole());
    }

    @Override
    public String getCommunityType() {
      return "PERSONAL";
    }
  }

  static final class TestRole implements RoleDescriptor {
    @Override
    public String getName() {
      return "ADMIN";
    }

    @Override
    public boolean isAdmin() {
      return true;
    }
  }
}
