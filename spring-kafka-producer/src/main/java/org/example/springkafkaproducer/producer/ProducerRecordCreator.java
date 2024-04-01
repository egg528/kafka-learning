package org.example.springkafkaproducer.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.springkafkaproducer.event.TestEvent;
import org.springframework.stereotype.Component;

public class ProducerRecordCreator {
    public static ProducerRecord<String, TestEvent> create(String topic, String key, String field1, String field2) {
        return new ProducerRecord<>(topic, key, new TestEvent(field1, field2));
    }

    public static ProducerRecord<String, String> create(String topic, String key, String value) {
        return new ProducerRecord<>(topic, key, value);
    }
}
