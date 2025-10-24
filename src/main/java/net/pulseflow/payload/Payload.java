package net.pulseflow.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Payload {

    UUID getId();
    LocalDateTime getCreatedAt();

}
