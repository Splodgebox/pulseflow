package net.pulseflow.broker;

import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

import java.util.List;

public interface IBroker {

    <T> void publish(Message<T> message);
    void subscribe(ConsumerPayload<?> consumer);
    void unsubscribe(ConsumerPayload<?> consumer);
    int getConsumerCount();

    List<ConsumerPayload<?>> getConsumers();

    List<Message<?>> getQueuedMessages();

    void pause();

    void resume();

}
