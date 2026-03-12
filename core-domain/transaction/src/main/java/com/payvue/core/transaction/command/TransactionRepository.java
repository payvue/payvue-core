package com.payvue.core.transaction.command;

import java.util.Optional;

public interface TransactionRepository {

    void save(Transaction transaction);
    Optional<Transaction> findById(TransactionId id);
}
