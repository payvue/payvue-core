package com.payvue.generic.payment;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.common.status.ExecutionState;

import java.util.Map;
import java.util.Objects;

public final class Payment
        extends AggregateRoot<PaymentId, Payment> implements Identifiable<PaymentId> {

    private final PaymentId id;
    private PaymentStatus status;
    private final PaymentModeType paymentModeType;
    private final ProviderId providerId;
    private final String externalTransactionId;
    private final Map<String, String> metadata;

    private Payment(
            PaymentId id,
            PaymentStatus status,
            PaymentModeType paymentModeType,
            ProviderId providerId,
            String externalTransactionId,
            Map<String, String> metadata
    ) {
        this.id = Objects.requireNonNull(id, "paymentId는 필수입니다");
        this.status = Objects.requireNonNull(status, "status는 필수입니다");
        this.paymentModeType = Objects.requireNonNull(paymentModeType, "paymentModeType은 필수입니다");
        this.providerId = Objects.requireNonNull(providerId, "providerId는 필수입니다");
        this.externalTransactionId = externalTransactionId;
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public static Payment of(
            PaymentStatus status,
            PaymentModeType paymentModeType,
            ProviderId providerId,
            String externalTransactionId,
            Map<String, String> metadata
    ) {
        return new Payment(PaymentId.generate(), status, paymentModeType, providerId, externalTransactionId, metadata);
    }

    public static Payment of(
            PaymentId id,
            PaymentStatus status,
            PaymentModeType paymentModeType,
            ProviderId providerId,
            String externalTransactionId,
            Map<String, String> metadata
    ) {
        return new Payment(id, status, paymentModeType, providerId, externalTransactionId, metadata);
    }

    public static Payment reconstitute(
            PaymentId id,
            PaymentStatus status,
            PaymentModeType paymentModeType,
            ProviderId providerId,
            String externalTransactionId,
            Map<String, String> metadata
    ) {
        return new Payment(id, status, paymentModeType, providerId, externalTransactionId, metadata);
    }

    public void mergeWith(Map<String, String> mergedMetadata) {
        metadata.putAll(mergedMetadata);
    }

    @Override
    public PaymentId getId() {
        return id;
    }

    public PaymentStatus status() { return status; }
    public PaymentModeType paymentModeType() { return paymentModeType; }
    public ProviderId providerId() { return providerId; }
    public String externalTransactionId() { return externalTransactionId; }
    public Map<String,String> metadata() { return metadata; }

    public void approve() {
        status = PaymentStatus.APPROVED;
    }
}
