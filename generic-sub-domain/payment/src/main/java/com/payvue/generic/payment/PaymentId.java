package com.payvue.generic.payment;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PaymentId {

    private String value;

    protected PaymentId() { }

    public PaymentId(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public static PaymentId of(String id) {
        return new PaymentId(id);
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentId)) {
            return false;
        }
        PaymentId paymentId = (PaymentId) o;
        return Objects.equals(value, paymentId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }
}
