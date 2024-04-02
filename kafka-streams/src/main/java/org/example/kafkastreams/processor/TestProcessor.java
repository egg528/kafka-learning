package org.example.kafkastreams.processor;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.example.kafkastreams.event.TestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestProcessor {
    private static final String SOURCE_TOPIC = "test";
    private static final String TARGET_TOPIC = "test2";

    @Autowired
    void pipeline(StreamsBuilder streamsBuilder) {
        streamsBuilder.stream(SOURCE_TOPIC, Consumed.with(SerdeUtils.String(), SerdeUtils.TestEvent()))
                .map((key, value) -> KeyValue.pair(key, new TestEvent(value.field1().toUpperCase(), value.field2().toUpperCase())))
                .to(TARGET_TOPIC, Produced.with(SerdeUtils.String(), SerdeUtils.TestEvent()));
    }
}
