package net.pulseflow;

import lombok.NoArgsConstructor;
import net.pulseflow.broker.AsyncBroker;
import net.pulseflow.broker.SyncBroker;
import net.pulseflow.model.Message;

@NoArgsConstructor
public class Pulseflow {

    public static SyncBroker createDefaultBroker() {
        return new SyncBroker();
    }

    public static AsyncBroker createAsyncBroker() {
        return new AsyncBroker();
    }

    public static AsyncBroker createAsyncBroker(int threadCount) {
        return new AsyncBroker(threadCount);
    }

    public static <T> Message<T> createMessage(String topic, T content) {
        return Message.<T>builder()
                .content(content)
                .build();
    }

}
