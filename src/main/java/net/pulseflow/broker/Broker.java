package net.pulseflow.broker;

import lombok.RequiredArgsConstructor;
import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Broker implements IBroker {

    private final List<ConsumerPayload<?>> consumers = new ArrayList<>();

    @Override
    public void register(ConsumerPayload<?> consumer) {
        consumers.add(consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void publish(Message<T> message) {
        for (ConsumerPayload<?> consumer : consumers) {
            ConsumerPayload<T> typed = (ConsumerPayload<T>) consumer;
            if (typed.canHandle(message)) {
                typed.handleMessage(message);
            }
        }
    }
}
