package net.pulseflow.broker;

import net.pulseflow.payload.consumer.ConsumerPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SyncBrokerTest {

    private SyncBroker broker;

    @BeforeEach
    void setUp() {
        broker = new SyncBroker();
    }

    @Test
    void testSubscribeSuccessfullyAddsConsumer() {
        ConsumerPayload<?> consumer = mock(ConsumerPayload.class);

        broker.subscribe(consumer);

        assertEquals(1, broker.getConsumerCount());
        assertTrue(broker.getConsumers().contains(consumer));
    }

    @Test
    void testSubscribeMultipleConsumers() {
        ConsumerPayload<?> consumer1 = mock(ConsumerPayload.class);
        ConsumerPayload<?> consumer2 = mock(ConsumerPayload.class);

        broker.subscribe(consumer1);
        broker.subscribe(consumer2);

        assertEquals(2, broker.getConsumerCount());
        assertTrue(broker.getConsumers().contains(consumer1));
        assertTrue(broker.getConsumers().contains(consumer2));
    }

    @Test
    void testSubscribeThrowsExceptionWhenConsumerIsNull() {
        assertThrows(IllegalArgumentException.class, () -> broker.subscribe(null));
    }

    @Test
    void testSubscribeNotifiesListeners() {
        ConsumerPayload<?> consumer = mock(ConsumerPayload.class);
        BrokerListener listener = mock(BrokerListener.class);
        broker.addListener(listener);

        broker.subscribe(consumer);

        verify(listener, times(1)).onConsumerSubscribed(consumer);
    }
}