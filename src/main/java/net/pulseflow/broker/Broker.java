package net.pulseflow.broker;

import lombok.extern.log4j.Log4j2;
import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
public class Broker implements IBroker {

    private final List<ConsumerPayload<?>> consumers = new CopyOnWriteArrayList<>();

    @Override
    public void subscribe(ConsumerPayload<?> consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer cannot be null");
        }
        consumers.add(consumer);
        log.debug("Consumer subscribed: {}. Total consumers: {}", consumer.getClass().getSimpleName(), consumers.size());
    }

    @Override
    public void unsubscribe(ConsumerPayload<?> consumer) {
        boolean removed = consumers.remove(consumer);
        if (removed) {
            log.debug("Consumer unsubscribed: {}. Total consumers: {}", consumer.getClass().getSimpleName(), consumers.size());
        }
    }

    @Override
    public int getConsumerCount() {
        return consumers.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void publish(Message<T> message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        log.debug("Publishing message to topic '{}' from sender '{}'", message.getTopic(), message.getSender());

        int deliveredCount = 0;
        for (ConsumerPayload<?> consumer : consumers) {
            try {
                ConsumerPayload<T> typedConsumer = (ConsumerPayload<T>) consumer;
                if (typedConsumer.canHandle(message)) {
                    typedConsumer.handleMessage(message);
                    deliveredCount++;
                    log.debug("Message delivered to consumer: {}", consumer.getClass().getSimpleName());
                }
            } catch (Exception e) {
                log.error("Error delivering message to consumer: {}. Message: {}",
                        consumer.getClass().getSimpleName(), message, e);
            }
        }

        if (deliveredCount == 0) {
            log.warn("No consumers handled message on topic '{}' from sender '{}'",
                    message.getTopic(), message.getSender());
        } else {
            log.debug("Message delivered to {} consumer(s)", deliveredCount);
        }
    }
}
