package com.payvue.supporting.interaction;

import java.util.Objects;

public abstract class Decision<I extends Intent> {
    
    private final I intent;
    
    protected Decision(I intent) {
        this.intent = Objects.requireNonNull(intent, "intent는 필수입니다");
    }
    public final I getIntent() {
        return intent;
    }

    public Interaction executeWith(InteractionContext context, DomainService service) {
        return service.execute(context, this);
    }
    public Interaction executeWith(DomainService service) {
        return service.execute(this);
    }
}
