package net.pulseflow.payload.producer;

import net.pulseflow.model.Message;
import net.pulseflow.payload.Payload;

public interface ProducerPayload<T> extends Payload {

    String getSender();
    Message<T> toMessage();

}
