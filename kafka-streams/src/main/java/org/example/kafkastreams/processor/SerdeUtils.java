package org.example.kafkastreams.processor;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.example.kafkastreams.event.TestEvent;
import org.springframework.kafka.support.serializer.JsonSerde;

public class SerdeUtils {

    public static Serde<String> String() {
        return Serdes.String();
    }

    public static Serde<TestEvent> TestEvent() {
        return new JsonSerde<>(TestEvent.class);
    }
}
