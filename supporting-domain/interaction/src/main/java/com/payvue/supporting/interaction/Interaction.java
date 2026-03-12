package com.payvue.supporting.interaction;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.common.status.ExecutionState;
import com.payvue.common.status.ExecutionStateSupport;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Interaction extends AggregateRoot<InteractionId, Interaction>
        implements Identifiable<InteractionId>, ExecutionStateSupport {
    
    private final InteractionId id;
    private final Decision<?> decision;
    @Setter
    private ExecutionState status;
    @Setter
    private String message;
    
    private Interaction(
        InteractionId id,
        Decision<?> decision,
        ExecutionState status
    ) {
        this.id = Objects.requireNonNull(id, "id는 필수입니다");
        this.decision = Objects.requireNonNull(decision, "decision은 필수입니다");
        this.status = Objects.requireNonNull(status, "status는 필수입니다");
    }

    public static Interaction pending(Decision<?> decision) {
        return new Interaction(InteractionId.generate(), decision, ExecutionState.PENDING_APPROVAL);
    }

    @Override
    public boolean isPendingApproval() {
        return status != null && status.isPendingApproval();
    }

    @Override
    public boolean isCompleted() {
        return status != null && status.isCompleted();
    }



}
