package com.payvue.generic.payment;

public interface PaymentTranslator<T extends PaymentProviderResponse> {

  ProviderId providerId();

  /**
   * 지원하는 결제 모드 여부. 기본값은 모든 모드를 허용한다.
   */
  default boolean supports(PaymentModeType mode) {
    return true;
  }

  Payment translate(T response);
}
