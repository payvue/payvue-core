package com.payvue.core.community.policy;

import com.payvue.core.community.Community;

public abstract class ChainableRule implements Rule {

    private ChainableRule nextRule;

    public ChainableRule append(ChainableRule rule) {
        if (this.nextRule == null) {
            this.nextRule = rule;
        } else {
            this.nextRule.append(rule);
        }
        return this;
    }

    public boolean evaluateChain(Community community) {
        boolean currentResult = evaluate(community);
        
        if (!currentResult) {
            return false;
        }
        
        if (hasNext()) {
            return nextRule.evaluateChain(community);
        }
        return true;
    }

    public boolean hasNext() {
        return nextRule != null;
    }

}
