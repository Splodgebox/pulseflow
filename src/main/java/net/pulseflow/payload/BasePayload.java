package net.pulseflow.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BasePayload implements Payload {

    private final UUID id = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
