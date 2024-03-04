package org.example.springkafkaconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "test", containerFactory = "concurrentKafkaMessageListenerContainerFactory")
    public void listenTestTopic(
            String message) {
        logger.info("Received Message: {}", message);
    }
}
