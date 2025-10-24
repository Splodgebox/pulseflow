# Pulseflow - (Work in Progress)

Pulseflow is a lightweight, autonomous message broker framework written in Java.
It’s designed to let producers and consumers communicate seamlessly - no complex setup, no static globals, just clean, object-oriented flow.

------------------------------------------------------------
Features
------------------------------------------------------------
- Instantiable Broker - create multiple isolated brokers with ease.
- Producer / Consumer Payloads - fully type-safe message passing.
- Generic Message<T> System - send any data type, not just strings.
- Autonomous Routing - the broker automatically dispatches messages to matching consumers.
- Sync and Async message routing - You can setup async message routing to consumers.
- Filter Logic via canHandle() - consumers decide which messages they want.
- Extensible Architecture - ready for caching, async routing, or machine learning optimizations.

------------------------------------------------------------
Architecture Overview
------------------------------------------------------------
```
Producer  →  Broker  →  Consumer
   │           │           │
   │  Message  │  routes   │  handle()
   └──────────▶│──────────▶│
```

- Producer creates a Message<T>
- Broker routes it to all registered consumers that can handle it
- Consumer processes the message if canHandle() returns true

------------------------------------------------------------
Code Examples
------------------------------------------------------------
Setting up the broker
```java
// Setup normal broker
SyncBroker broker = Pulseflow.createDefaultBroker();

// Setup a asynchronous broker
AsyncBroker asyncBroker = Pulseflow.createAsyncBroker();
```
Create Consumer
```java
@Log4j2
public class Consumer extends BasePayload implements ConsumerPayload<String> {
    @Override
    public boolean canHandle(Message<String> message) {
        return message.getTopic().equalsIgnoreCase("newsletter/tech");
    }

    @Override
    public void handleMessage(Message<String> message) {
        log.info("Received message: {}", message);
    }
}
```

Create Message/Producer
You can make a generic producer and just make it sign the messages rather than hardcode the message
```java
    public Message<String> createMessage() {
        return Message.<String>builder()
                .topic("message/tech")
                .sender("system")
                .content("Hello World!")
                .build();
    }

public class Producer extends BasePayload implements ProducerPayload<String> {
    @Override
    public String getSender() {
        return "system";
    }

    @Override
    public Message<String> toMessage() {
        return Message.<String>builder()
                .sender(getSender())
                .topic("message/tech")
                .content("Hello World!")
                .build();
    }
}
```

Link it all together
```java

public class Demo {
    public static void main(String[] args) {
        SyncBroker broker = new SyncBroker();

        Message<String> message = Message.<String>builder()
                .topic("message/tech")
                .content("Hello World!")
                .build();
        
        broker.subscribe(new Consumer());
        broker.publish(message);
    }
}
```

You can also listen for certain events
```java
public interface BrokerListener {
    void onMessageDispatched(Message<?> message);
    void onMessageReceived(Message<?> message);
    void onConsumerSubscribed(ConsumerPayload<?> consumer);
    void onConsumerUnsubscribed(ConsumerPayload<?> consumer);
}

broker.addListener(new BrokerListener());
```

------------------------------------------------------------
Future Enhancements
------------------------------------------------------------
- Smart (ML-driven) message filtering
- Lightweight JSON serialization for networked messaging

------------------------------------------------------------
License
------------------------------------------------------------
MIT License © 2025 - Developed by Splodgebox
Feel free to use, modify, and contribute.
