package net.pulseflow.broker;

import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

public interface BrokerListener {
    void onMessageDispatched(Message<?> message);

    void onMessageReceived(Message<?> message);

    void onConsumerSubscribed(ConsumerPayload<?> consumer);

    void onConsumerUnsubscribed(ConsumerPayload<?> consumer);
}