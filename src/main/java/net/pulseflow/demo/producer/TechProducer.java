package net.pulseflow.demo.producer;

import lombok.RequiredArgsConstructor;
import net.pulseflow.model.Message;
import net.pulseflow.payload.BasePayload;
import net.pulseflow.payload.producer.ProducerPayload;

@RequiredArgsConstructor
public class TechProducer extends BasePayload implements ProducerPayload<String> {

    private final String topic;
    private final String text;

    @Override
    public String getSender() {
        return "Bloomberg";
    }

    @Override
    public Message<String> toMessage() {
        return Message.<String>builder()
                .topic(topic)
                .content(text)
                .sender(getSender())
                .build();
    }
}
