package org.bapinaev.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "account_events")
public class AccountEventEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "accountId", nullable = false)
    private String accountId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "old_data", nullable = false)
    private String oldData;

    @Column(name = "new_data", nullable = false)
    private String newData;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    public AccountEventEntity(String login, String eventType, String oldData, String newData, Instant timestamp) {
        this.accountId = login;
        this.eventType = eventType;
        this.oldData = oldData;
        this.newData = newData;
        this.timestamp = timestamp;
    }

    public AccountEventEntity() {
    }
}
