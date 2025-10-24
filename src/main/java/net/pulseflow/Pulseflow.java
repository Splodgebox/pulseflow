package net.pulseflow;

import lombok.NoArgsConstructor;
import net.pulseflow.broker.AsyncBroker;
import net.pulseflow.broker.SyncBroker;

@NoArgsConstructor
public class Pulseflow {

    public static SyncBroker createDefaultBroker() {
        return new SyncBroker();
    }

    public static AsyncBroker createAsyncBroker() {
        return new AsyncBroker();
    }

}
