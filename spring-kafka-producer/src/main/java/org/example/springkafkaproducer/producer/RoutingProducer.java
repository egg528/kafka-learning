package org.example.springkafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import org.example.springkafkaproducer.event.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Service;

// @Service
@RequiredArgsConstructor
public class RoutingProducer implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(RoutingProducer.class);

    private final RoutingKafkaTemplate routingKafkaTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        routingKafkaTemplate.send("test", "key:1234", new TestEvent("value1", "value2"));
        routingKafkaTemplate.send("test2", "key:1234", "value1");
    }
}
