package com.payvue.generic.payment;

import java.util.UUID;

public class NoOpPaymentGateway implements PaymentGateway<PaymentProviderResponse> {
    private final ProviderId providerId;
    public NoOpPaymentGateway(ProviderId providerId) { this.providerId = providerId; }
    @Override public ProviderId providerId() { return providerId; }
    @Override public boolean supports(PaymentModeType mode) { return true; }
    @Override public PaymentProviderResponse reserve(OrderItem order) { throw new UnsupportedOperationException("지원하지 않는 게이트웨이입니다."); }
    @Override public PaymentProviderResponse confirm(String paymentId) { throw new UnsupportedOperationException("지원하지 않는 게이트웨이입니다."); }
    @Override public PaymentProviderResponse cancel(String paymentId, String reason) { throw new UnsupportedOperationException("지원하지 않는 게이트웨이입니다."); }
    @Override public PaymentProviderResponse history(String startTime, String endTime) { throw new UnsupportedOperationException("지원하지 않는 게이트웨이입니다."); }
}
