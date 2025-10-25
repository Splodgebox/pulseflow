package net.pulseflow.broker;

import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public interface IBroker {

    <T> void publish(Message<T> message);
    <T> void publishDelayed(Message<T> message, long delay, TimeUnit timeUnit);
    void subscribe(ConsumerPayload<?> consumer);
    void unsubscribe(ConsumerPayload<?> consumer);
    int getConsumerCount();
    List<ConsumerPayload<?>> getConsumers();
    Queue<Message<?>> getQueuedMessages();
    void pause();
    void resume();

}
