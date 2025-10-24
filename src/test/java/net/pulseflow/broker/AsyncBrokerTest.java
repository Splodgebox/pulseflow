package net.pulseflow.broker;

import net.pulseflow.model.Message;
import net.pulseflow.payload.consumer.ConsumerPayload;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the AsyncBroker class, focusing on asynchronous message publishing.
 * <p>
 * Verifies that the broker correctly:
 * - Validates input messages
 * - Queues messages when paused
 * - Notifies consumers asynchronously
 * - Notifies listeners after message dispatch
 * - Handles consumer exceptions gracefully
 */
class AsyncBrokerTest {

    private AsyncBroker asyncBroker;

    @BeforeEach
    void setUp() {
        asyncBroker = new AsyncBroker();
    }

    @AfterEach
    void tearDown() {
        asyncBroker.close();
    }

    @Test
    void testPublishThrowsExceptionForNullMessage() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> asyncBroker.publish(null)
        );
        assertEquals("Message cannot be null", exception.getMessage());
    }

    @Test
    void testPublishQueuesMessagesWhenBrokerIsPaused() {
        asyncBroker.pause();
        Message<String> message = Message.<String>builder().build();

        asyncBroker.publish(message);

        assertEquals(1, asyncBroker.getQueuedMessages().size());
        assertTrue(asyncBroker.getQueuedMessages().contains(message));
    }

    @Test
    void testPublishNotifiesRegisteredConsumers() throws InterruptedException {
        ConsumerPayload<String> mockConsumer = mock(ConsumerPayload.class);
        when(mockConsumer.canHandle(any())).thenReturn(true);
        asyncBroker.subscribe(mockConsumer);

        Message<String> message = Message.<String>builder().build();
        asyncBroker.publish(message);

        Thread.sleep(100); // Allow async execution to complete

        verify(mockConsumer, times(1)).handleMessage(message);
    }

    @Test
    void testPublishNotifiesListenersAfterDispatch() {
        BrokerListener mockListener = mock(BrokerListener.class);
        ConsumerPayload<String> mockConsumer = mock(ConsumerPayload.class);
        when(mockConsumer.canHandle(any())).thenReturn(true);

        asyncBroker.addListener(mockListener);
        asyncBroker.subscribe(mockConsumer);

        Message<String> message = Message.<String>builder().build();
        asyncBroker.publish(message);

        verify(mockListener, times(1)).onMessageDispatched(message);
    }

    @Test
    void testPublishHandlesConsumerExceptionGracefully() {
        ConsumerPayload<String> mockConsumer = mock(ConsumerPayload.class);
        when(mockConsumer.canHandle(any())).thenReturn(true);
        doThrow(new RuntimeException("Test Exception")).when(mockConsumer).handleMessage(any());
        asyncBroker.subscribe(mockConsumer);

        Message<String> message = Message.<String>builder()
                .id(UUID.randomUUID())
                .build();

        assertDoesNotThrow(() -> asyncBroker.publish(message));
    }
}