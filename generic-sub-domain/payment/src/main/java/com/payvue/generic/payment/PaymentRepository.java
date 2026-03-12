package com.payvue.generic.payment;

public interface PaymentRepository {

    void save(Payment payment);
    Payment findById(PaymentId id);
}
