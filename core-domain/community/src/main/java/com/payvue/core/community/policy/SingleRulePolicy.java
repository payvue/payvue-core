package com.payvue.core.community.policy;


import com.payvue.core.community.Community;

public class SingleRulePolicy implements CommunityPolicy {

    private final Rule rule;

    private SingleRulePolicy(Rule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("규칙은 null일 수 없습니다.");
        }
        this.rule = rule;
    }

    public static SingleRulePolicy of(Rule rule) {
        return new SingleRulePolicy(rule);
    }

    @Override
    public boolean isSatisfied(Community community) {
        return rule.evaluate(community);
    }

}
