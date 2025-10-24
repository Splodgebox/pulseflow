package net.pulseflow.payload.consumer;

import net.pulseflow.model.Message;
import net.pulseflow.payload.Payload;

public interface ConsumerPayload<T> extends Payload {

    boolean canHandle(Message<T> message);
    void handleMessage(Message<T> message);

}
