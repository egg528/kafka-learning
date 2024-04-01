package org.example.springkafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.springkafkaproducer.event.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

// @Service
@RequiredArgsConstructor
public class NonBlockingProducer implements ApplicationRunner {
    private final static Logger logger = LoggerFactory.getLogger(NonBlockingProducer.class);

    private final KafkaTemplate<String, TestEvent> kafkaTemplate;

    @Override
    public void run(ApplicationArguments args) {
        ProducerRecord<String, TestEvent> record =
                ProducerRecordCreator.create("test", "key", "value1", "value2");

        CompletableFuture<SendResult<String, TestEvent>> future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info(result.toString());
            } else {
                logger.error(ex.getMessage());
            }
        });
    }
}
