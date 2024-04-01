package org.example.springkafkaproducer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ReplyingConsumer {

    @SendTo
    @KafkaListener(id = "test-request-id", topics = "test")
    public String consume(String message) {
        return "consume success -> message: %s".formatted(message);
    }
}
