package org.bapinaev.events;

import java.time.Instant;

public record Event(
        String id,
        String eventType,
        String oldData,
        String newData,
        Instant timestamp


) {
    @Override
    public String id() {
        return id;
    }

    @Override
    public String eventType() {
        return eventType;
    }

    @Override
    public String oldData() {
        return oldData;
    }

    @Override
    public String newData() {
        return newData;
    }

    @Override
    public Instant timestamp() {
        return timestamp;
    }
}
