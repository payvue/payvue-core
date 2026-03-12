package com.payvue.generic.payment;

public interface PaymentGateway<R extends PaymentProviderResponse> {

  ProviderId providerId();

  boolean supports(PaymentModeType mode);

  R reserve(OrderItem order);

  R confirm(String paymentId);

  R cancel(String paymentId, String reason);

  R history(String startTime, String endTime);
}
