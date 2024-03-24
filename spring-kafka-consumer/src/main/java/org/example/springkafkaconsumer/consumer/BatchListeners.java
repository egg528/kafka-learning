package org.example.springkafkaconsumer.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

// Non-Blocking Retries are not supported with batch listeners.
@Component
public class BatchListeners {
    private final static Logger logger = LoggerFactory.getLogger(BatchListeners.class);


    // message를 List로 받아오게 된다.
    // id값은 Kafka Consumer group.idf를 뜻한다.
//    @KafkaListener(id = "list", topics = "test")
//    public void listen(List<String> list) {
//        logger.info(list); // [1, 2, 3, 1, 3, 1, 3, 1, 3]
//    }


    // Header 정보들도 List로 받아오게 된다.
//    @KafkaListener(id = "list", topics = "test")
//    public void listen(List<String> list,
//                       @Header(KafkaHeaders.RECEIVED_KEY) List<Integer> keys,
//                       @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
//                       @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
//                       @Header(KafkaHeaders.OFFSET) List<Long> offsets
//    ) {
//        logger.info("=========POLL=========");
//        logger.info(list.toString()); // [1, 2, 3, 4]
//        logger.info(keys.toString()); // [null, null, null, null]
//        logger.info(partitions.toString()); // [0, 0, 0, 0]
//        logger.info(topics.toString()); // [test, test, test, test]
//        logger.info(offsets.toString()); // [73, 74, 75, 76]
//        logger.info("=========COMMIT=========");
//    }


    // ConsumerRecord 객체를 활용해 key, value와 다양한 KafkaHeader값을 단일 객체로 받아 처리할 수 있다.
//    @KafkaListener(id = "list", topics = "test")
//    public void listen(List<ConsumerRecord<Integer, String>> list) {
//
//        list.stream()
//            .map(ConsumerRecord::value)
//            .forEach(logger::info);
//    }


    // ConsumerRecords 객체를 활용해 poll()을 통해 얻은 ConsumerRecord들을 Iterable형태로 받을 수 있다.
//    @KafkaListener(id = "list", topics = "test")
//    public void listen(ConsumerRecords<Integer, String> records) {
//        for (ConsumerRecord<Integer, String> record : records) {
//            logger.info(record.value());
//        }
//    }
}
