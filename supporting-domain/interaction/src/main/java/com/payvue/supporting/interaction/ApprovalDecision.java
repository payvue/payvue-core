package com.payvue.supporting.interaction;

abstract class ApprovalDecision<I extends Intent & RequiresApproval> extends Decision<I> {
    protected ApprovalDecision(I intent) {
        super(intent);
    }
}
