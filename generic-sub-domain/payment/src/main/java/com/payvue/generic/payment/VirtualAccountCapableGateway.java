package com.payvue.generic.payment;

import com.payvue.generic.payment.OrderItem;

public abstract class VirtualAccountCapableGateway<R extends PaymentProviderResponse>
    implements PaymentGateway<R> {

  @Override
  public boolean supports(PaymentModeType mode) {
    return mode == PaymentModeType.VIRTUAL_ACCOUNT;
  }

  @Override
  public R reserve(OrderItem order) {
    return reserveVirtual(order, defaultBankCode());
  }

  public abstract R reserveVirtual(OrderItem order, String bankCode);

  protected String resolveBankCode(String requestedBankCode) {
    if (requestedBankCode != null && !requestedBankCode.isBlank()) {
      if (!supportsBankCode(requestedBankCode)) {
        throw new IllegalArgumentException("지원하지 않는 은행 코드입니다: " + requestedBankCode);
      }
      return requestedBankCode;
    }
    return defaultBankCode();
  }

  protected abstract String defaultBankCode();

  protected boolean supportsBankCode(String bankCode) {
    return true;
  }
}
