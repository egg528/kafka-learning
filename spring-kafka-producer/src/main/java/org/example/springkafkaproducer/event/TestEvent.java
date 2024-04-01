package org.example.springkafkaproducer.event;

import org.apache.kafka.clients.producer.ProducerRecord;

public record TestEvent(
        String field1,
        String field2
) {
}
