package net.pulseflow.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Message<T> {

    @Builder.Default
    private final UUID id = UUID.randomUUID();

    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();

    private final String topic;
    private final String sender;

    @NonNull
    private final T content;
}
