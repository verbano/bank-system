package org.bapinaev.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "user_events")
public class UserEventEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "old_data", columnDefinition = "TEXT")
    private String oldData;

    @Column(name = "new_data", columnDefinition = "TEXT")
    private String newData;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    public UserEventEntity(String login, String eventType, String oldData, String newData, Instant timestamp) {
        this.login = login;
        this.eventType = eventType;
        this.oldData = oldData;
        this.newData = newData;
        this.timestamp = timestamp;
    }

    public UserEventEntity() {
    }
}
