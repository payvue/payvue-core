package com.payvue.generic.payment;

public class NoOpPaymentTranslator implements PaymentTranslator<PaymentProviderResponse> {
    private final ProviderId providerId;
    public NoOpPaymentTranslator(ProviderId providerId) { this.providerId = providerId; }
    @Override public ProviderId providerId() { return providerId; }
    @Override public boolean supports(PaymentModeType mode) { return true; }
    @Override public Payment translate(PaymentProviderResponse response) { throw new UnsupportedOperationException("지원하지 않습니다."); }
}