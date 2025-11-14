package org.bapinaev.models.transactions;

import org.bapinaev.enums.TransactionType;
import org.bapinaev.models.accounts.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final UUID id;

    private final Account account;

    private final LocalDateTime timestamp;

    private final BigDecimal amount;

    private final TransactionType type;

    public Transaction(Account account, BigDecimal amount, TransactionType type) {
        this.account = account;
        this.amount = amount;
        this.type = type;

        id = UUID.randomUUID();
        timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}