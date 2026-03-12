package com.payvue.core.transaction.command;

import com.payvue.common.domain.AggregateRoot;
import com.payvue.common.domain.Identifiable;
import com.payvue.common.status.ExecutionState;
import com.payvue.core.asset.AssetItem;
import com.payvue.core.community.Community;
import com.payvue.core.transaction.command.meta.TransactionMeta;
import com.payvue.supporting.user.command.UserId;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

public final class Transaction
    extends AggregateRoot<TransactionId, Transaction>
    implements Identifiable<TransactionId> {

  private final TransactionId id;
  private final TransactionType type;
  private final String assetType;
  private final Community from;
  private final Community to;
  private final AssetItem item;
  private final Long amount;
  private final UserId actorId;
  private final String description;
  private final String category;
  private final ExecutionState interactionState;
  private final Instant occurredAt;
  private final TransactionMeta meta;

  private Transaction(
      TransactionId id,
      TransactionType type,
      String assetType,
      Community from,
      Community to,
      AssetItem item,
      Long amount,
      UserId actorId,
      String description,
      String category,
      ExecutionState interactionState,
      Instant occurredAt,
      TransactionMeta meta
  ) {
    this.id = Objects.requireNonNull(id, "id는 필수입니다.");
    this.type = Objects.requireNonNull(type, "type은 필수입니다.");
    this.assetType = Objects.requireNonNull(assetType, "assetType은 필수입니다.")
        .toUpperCase(Locale.ROOT);
    this.from = Objects.requireNonNull(from, "from은 필수입니다.");
    this.to = Objects.requireNonNull(to, "to는 필수입니다.");
    this.item = Objects.requireNonNull(item, "item은 필수입니다.");
    this.amount = Objects.requireNonNull(amount, "amount는 필수입니다.");
    this.actorId = Objects.requireNonNull(actorId, "actorId는 필수입니다.");
    this.description = description;
    this.category = category;
    this.interactionState = Objects.requireNonNull(interactionState, "interactionState는 필수입니다.");
    this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt은 필수입니다.");
    this.meta = Objects.requireNonNull(meta, "meta는 필수입니다.");
  }

  public static Transaction pending(
      TransactionCommand cmd,
      ExecutionState interactionState
  ) {
    return new Transaction(
        TransactionId.generate(),
        cmd.type(),
        cmd.assetType(),
        cmd.from(),
        cmd.to(),
        cmd.item(),
        cmd.amount().toLong(),
        cmd.actor().actorId(),
        cmd.item().getName(),
        cmd.item().getCategory().getValue(),
        Objects.requireNonNull(interactionState, "interactionState는 필수입니다."),
        Instant.now(),
        cmd.meta()
    );
  }

  public static Transaction completed(
      TransactionCommand cmd,
      ExecutionState interactionState
  ) {
    return new Transaction(
        TransactionId.generate(),
        cmd.type(),
        cmd.assetType(),
        cmd.from(),
        cmd.to(),
        cmd.item(),
        cmd.amount().toLong(),
        cmd.actor().actorId(),
        cmd.item().getName(),
        cmd.item().getCategory().getValue(),
        Objects.requireNonNull(interactionState, "interactionState는 필수입니다."),
        Instant.now(),
        cmd.meta()
    );
  }

  public static Transaction failed(
      TransactionCommand cmd,
      ExecutionState interactionState
  ) {
    return new Transaction(
        TransactionId.generate(),
        cmd.type(),
        cmd.assetType(),
        cmd.from(),
        cmd.to(),
        cmd.item(),
        cmd.amount().toLong(),
        cmd.actor().actorId(),
        cmd.item().getName(),
        cmd.item().getCategory().getValue(),
        Objects.requireNonNull(interactionState, "interactionState는 필수입니다."),
        Instant.now(),
        cmd.meta()
    );
  }

  public static Transaction reconstitute(
      TransactionId id,
      TransactionType type,
      String assetType,
      Community from,
      Community to,
      AssetItem item,
      Long amount,
      UserId actorId,
      String description,
      String category,
      ExecutionState interactionState,
      Instant occurredAt,
      TransactionMeta meta
  ) {
    return new Transaction(
        id,
        type,
        assetType,
        from,
        to,
        item,
        amount,
        actorId,
        description,
        category,
        interactionState,
        occurredAt,
        meta
    );
  }

  @Override
  public TransactionId getId() {
    return id;
  }

  public TransactionType type() { return type; }
  public String assetType() { return assetType; }
  public Community from() { return from; }
  public Community to() { return to; }
  public Community community() { return from; }
  public AssetItem item() { return item; }
  public Long amount() { return amount; }
  public UserId actorId() { return actorId; }
  public String description() { return description; }
  public String category() { return category; }
  public ExecutionState interactionState() { return interactionState; }
  public Instant occurredAt() { return occurredAt; }
  public TransactionMeta meta() { return meta; }
}
