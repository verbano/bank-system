package org.bapinaev.entities.transactions;

import jakarta.persistence.*;
import org.bapinaev.enums.TransactionType;
import org.bapinaev.entities.accounts.AccountEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private AccountEntity account;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "amount", precision = 20, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    public TransactionEntity() {
    }

    public TransactionEntity(AccountEntity account, BigDecimal amount, TransactionType type) {
        this.account = account;
        this.amount = amount;
        this.type = type;

        id = UUID.randomUUID();
        timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public AccountEntity getAccount() {
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
