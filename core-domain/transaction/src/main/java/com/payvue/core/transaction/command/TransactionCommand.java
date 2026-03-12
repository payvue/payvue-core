package com.payvue.core.transaction.command;

import com.payvue.core.asset.AssetItem;
import com.payvue.core.community.Community;
import com.payvue.core.transaction.command.meta.TransactionMeta;
import com.payvue.shared.Money;

import java.util.Locale;
import java.util.Objects;

public record TransactionCommand(
    TransactionType type,
    String assetType,
    AssetItem item,
    Money amount,
    Actor actor,
    TransactionMeta meta,
    Community from,
    Community to
) {
  public TransactionCommand {
    Objects.requireNonNull(type, "type은 필수입니다.");
    Objects.requireNonNull(assetType, "assetType은 필수입니다.");
    Objects.requireNonNull(item, "assetItem은 필수입니다.");
    Objects.requireNonNull(amount, "amount는 필수입니다.");
    Objects.requireNonNull(actor, "actor는 필수입니다.");
    Objects.requireNonNull(meta, "meta는 필수입니다.");
    Objects.requireNonNull(from, "from은 필수입니다.");
    Objects.requireNonNull(to, "to는 필수입니다.");
    assetType = assetType.toUpperCase(Locale.ROOT);
  }
}
