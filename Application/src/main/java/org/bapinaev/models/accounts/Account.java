package org.bapinaev.models.accounts;

import org.bapinaev.models.transactions.Transaction;
import org.bapinaev.models.users.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private UUID id;

    private BigDecimal balance;

    private final User owner;

    private final List<Transaction> transactions;

    public Account(User owner) {
        this.balance = BigDecimal.ZERO;
        this.owner = owner;
        this.transactions = new ArrayList<>();
        this.id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}