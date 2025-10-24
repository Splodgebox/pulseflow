package net.pulseflow.broker;

import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

public interface IBroker {

    <T> void publish(Message<T> message);
    void register(ConsumerPayload<?> consumer);

}
