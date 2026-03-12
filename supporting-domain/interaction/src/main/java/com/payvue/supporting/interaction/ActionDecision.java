package com.payvue.supporting.interaction;

abstract class ActionDecision<I extends Intent & RequiresAction> extends Decision<I> {
    
    protected ActionDecision(I intent) {
        super(intent);
    }
}
