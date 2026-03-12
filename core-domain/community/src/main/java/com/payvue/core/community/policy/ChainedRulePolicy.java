package com.payvue.core.community.policy;

import com.payvue.core.community.Community;


public class ChainedRulePolicy implements CommunityPolicy {

    private final ChainableRule rule;

    private ChainedRulePolicy(ChainableRule rule) {
        this.rule = rule;
    }

    public static ChainedRulePolicy of(ChainableRule rule) {
        return new ChainedRulePolicy(rule);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean isSatisfied(Community community) {
        return rule.evaluateChain(community);
    }

    public static class Builder {

        private ChainableRule firstRule;
        private ChainableRule lastRule;

        public Builder append(ChainableRule rule) {
            if (rule == null) {
                throw new IllegalArgumentException("규칙은 null일 수 없습니다.");
            }

            if (firstRule == null) {
                firstRule = rule;
                lastRule = rule;
            } else {
                lastRule.append(rule);
                lastRule = rule;
            }

            return this;
        }

        public ChainedRulePolicy build() {
            if (firstRule == null) {
                throw new IllegalStateException("최소 하나의 규칙이 필요합니다.");
            }
            return new ChainedRulePolicy(firstRule);
        }
    }
}
