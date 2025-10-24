package net.pulseflow.demo;

import lombok.extern.log4j.Log4j2;
import net.pulseflow.broker.Broker;
import net.pulseflow.demo.consumer.SportConsumer;
import net.pulseflow.demo.consumer.StockConsumer;
import net.pulseflow.demo.consumer.TechConsumer;
import net.pulseflow.demo.producer.SportProducer;
import net.pulseflow.demo.producer.TechProducer;

@Log4j2
public class Demo {

    public static void main(String[] args) {
        SportProducer sportProducer = new SportProducer("newsletter/sport", "England has won the world cup... finally!");
        TechProducer techProducer = new TechProducer("newsletter/tech", "OpenAI is now allowing AI Girlfriends");

        SportConsumer sportConsumer = new SportConsumer();
        TechConsumer techConsumer = new TechConsumer();
        StockConsumer stockConsumer = new StockConsumer();

        Broker broker = new Broker();
        broker.subscribe(sportConsumer);
        broker.subscribe(techConsumer);
        broker.subscribe(stockConsumer);

        broker.publish(sportProducer.toMessage());
        broker.publish(techProducer.toMessage());

        log.info("Consumer Count: {}", broker.getConsumerCount());

        broker.unsubscribe(sportConsumer);

        log.info("Consumer Count: {}", broker.getConsumerCount());

        broker.publish(sportProducer.toMessage());
        broker.publish(techProducer.toMessage());
    }

}
