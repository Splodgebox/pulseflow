package net.pulseflow.broker;

import lombok.extern.log4j.Log4j2;
import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
public class SyncBroker implements IBroker {

    protected final List<ConsumerPayload<?>> consumers = new CopyOnWriteArrayList<>();
    protected final List<BrokerListener> listeners = new CopyOnWriteArrayList<>();
    protected final List<Message<?>> queuedMessages = new CopyOnWriteArrayList<>();
    protected boolean paused = false;

    @Override
    public void subscribe(ConsumerPayload<?> consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer cannot be null");
        }
        consumers.add(consumer);
        notifyConsumerSubscribed(consumer);
        log.debug("Consumer subscribed: {}. Total consumers: {}", consumer.getClass().getSimpleName(), consumers.size());
    }

    @Override
    public void unsubscribe(ConsumerPayload<?> consumer) {
        boolean removed = consumers.remove(consumer);
        if (removed) {
            notifyConsumerUnsubscribed(consumer);
            log.debug("Consumer unsubscribed: {}. Total consumers: {}", consumer.getClass().getSimpleName(), consumers.size());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void publish(Message<T> message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        notifyMessageReceived(message);

        if (paused) {
            queuedMessages.add(message);
            return;
        }

        for (ConsumerPayload<?> consumer : consumers) {
            try {
                ConsumerPayload<T> typedConsumer = (ConsumerPayload<T>) consumer;
                if (typedConsumer.canHandle(message)) {
                    typedConsumer.handleMessage(message);
                    notifyMessageDispatched(message);
                }
            } catch (Exception e) {
                log.error("Error delivering message to consumer: {}. Message: {}",
                        consumer.getClass().getSimpleName(), message, e);
            }
        }
    }

    @Override
    public int getConsumerCount() {
        return consumers.size();
    }

    @Override
    public List<ConsumerPayload<?>> getConsumers() {
        return Collections.unmodifiableList(consumers);
    }

    @Override
    public List<Message<?>> getQueuedMessages() {
        return Collections.unmodifiableList(queuedMessages);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
        queuedMessages.forEach(message -> publish(message));
    }

    public void addListener(BrokerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(BrokerListener listener) {
        listeners.remove(listener);
    }

    protected void notifyMessageReceived(Message<?> message) {
        for (BrokerListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

    protected void notifyMessageDispatched(Message<?> message) {
        for (BrokerListener listener : listeners) {
            listener.onMessageDispatched(message);
        }
    }

    protected void notifyConsumerSubscribed(ConsumerPayload<?> consumer) {
        for (BrokerListener listener : listeners) {
            listener.onConsumerSubscribed(consumer);
        }
    }

    protected void notifyConsumerUnsubscribed(ConsumerPayload<?> consumer) {
        for (BrokerListener listener : listeners) {
            listener.onConsumerUnsubscribed(consumer);
        }
    }
}
