package net.pulseflow.broker;

import lombok.extern.log4j.Log4j2;
import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
public class AsyncBroker extends SyncBroker implements AutoCloseable {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @SuppressWarnings("unchecked")
    @Override
    public <T> void publish(Message<T> message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        if (paused) {
            queuedMessages.add(message);
            return;
        }

        notifyMessageReceived(message);

        for (ConsumerPayload<?> consumer : consumers) {
            try {
                ConsumerPayload<T> typedConsumer = (ConsumerPayload<T>) consumer;
                if (typedConsumer.canHandle(message)) {
                    executor.submit(() -> {
                        typedConsumer.handleMessage(message);

                        for (BrokerListener brokerListener : listeners) {
                            brokerListener.onMessageDispatched(message);
                        }
                    });
                }
            } catch (Exception e) {
                log.error("Error delivering message to consumer: {}. Message: {}",
                        consumer.getClass().getSimpleName(), message, e);
            }
        }
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
