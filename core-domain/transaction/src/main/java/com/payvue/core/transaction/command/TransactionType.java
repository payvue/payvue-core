package com.payvue.core.transaction.command;

public enum TransactionType {
  ALL("모든 내역", "상품/서비스 내역"),
  TRANSFER("이체", "커뮤니티 내 자산 이체"),
  PAYMENT("결제", "상품/서비스 결제"),
  ACQUIRE("획득", "자산 획득"),
  REWARD("보상", "활동 보상 지급"),
  COMMISSION("수수료", "수수료 차감"),
  REFUND("환불", "결제 환불");

  private final String displayName;
  private final String description;

  TransactionType(String displayName, String description) {
    this.displayName = displayName;
    this.description = description;
  }

  public String displayName() {
    return displayName;
  }

  public String description() {
    return description;
  }

  public boolean isDebit() {
    return this == PAYMENT || this == COMMISSION || this == TRANSFER;
  }

  public boolean isCredit() {
    return this == REWARD || this == REFUND || this == ACQUIRE;
  }
}
