package org.example.springkafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.springkafkaproducer.event.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// @Service
@RequiredArgsConstructor
public class BlockingProducer implements ApplicationRunner {
    private final static Logger logger = LoggerFactory.getLogger(BlockingProducer.class);

    private final KafkaTemplate<String, TestEvent> kafkaTemplate;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ProducerRecord<String, TestEvent> record =
                ProducerRecordCreator.create("test", "key", "value1", "value2");

        try {
            kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);
            logger.info("produce success");
        }
        catch (ExecutionException | TimeoutException | InterruptedException ex) {
            logger.error(ex.getMessage());
        }

    }
}
