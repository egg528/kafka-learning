package org.example.springkafkaconsumer.consumer;

import org.example.springkafkaconsumer.domain.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeoutException;

@Component
public class RecordListeners {
    private final static Logger logger = LoggerFactory.getLogger(RecordListeners.class);

//    @KafkaListener(topics = "test", groupId = "test-group")
//    public void listen(String message) {
//        logger.info("Received Message: {}", message);
//    }

    // MANUAL AckMode를 사용할 경우 Acknowledgment를 제공받을 수 있다.
    // TODO: Acknowledgment의 정확한 역할 알아보기
//    @KafkaListener(topics = "test")
//    public void listen(String message, Acknowledgment ack) {
//        logger.info("Received Message: {}", message);
//    }

    // @Header를 통해서 원하는 Metadata에 접근할 수 있다
//    @KafkaListener(topics = "test")
//    public void listen(
//            @Payload String message,
//            @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
//            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
//    ) {
//        logger.info("Received Key: {}", key);
//        logger.info("Received Message: {}", message);
//    }


    // ConsumerRecordMetadata를 통해 Metadata에 접근할 수도 있다.
//    @KafkaListener(topics = "test")
//    public void listen(
//            String message,
//            ConsumerRecordMetadata meta
//    ) {
//        logger.info("Received Record Offset: {}", meta.offset());
//        logger.info("Received Message: {}", message);
//    }

    @KafkaListener(topics = "test", containerFactory = "concurrentKafkaListenerContainerFactory")
    @Transactional
    public void listen(TestEvent event) throws Exception {
        if (event.field2().equals("7")) {
            System.out.println(Thread.currentThread().getName());
            throw new Exception("==========consume fail==========");
        }

        logger.info("field1: {}, field2: {}", event.field1(), event.field2());
    }
}
