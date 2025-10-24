package net.pulseflow.demo.consumer;

import lombok.extern.log4j.Log4j2;
import net.pulseflow.model.Message;
import net.pulseflow.payload.BasePayload;
import net.pulseflow.payload.consumer.ConsumerPayload;

@Log4j2
public class StockConsumer extends BasePayload implements ConsumerPayload<String> {
    @Override
    public boolean canHandle(Message<String> message) {
        return message.getTopic().equals("newsletter/stock");
    }

    @Override
    public void handleMessage(Message<String> message) {
        log.info("Received message: {}", message);
    }
}
