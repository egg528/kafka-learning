package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.callback.AsyncCommitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AsyncCommitConsumer {
    private final static Logger logger = LoggerFactory.getLogger(AsyncCommitConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private final static String GROUP_ID = "test-group";

    public static void main(String[] args) {

        // 1. Consumer Config 설정
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID); // subscribe()로 토픽을 구독해서 사용할 때는 해당 옵션이 핋수이다 (default는 null)
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        // 2. Consumer 생성
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME)); // consumer에게 topic을 할당하는 코드. 1개 이상의 topic 할당이 가능하다.

        // 3. 지속적으로 Broker로부터 데이터를 poll()해서 처리한다.
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record: records) {
                logger.info("{}", record);
            }

            // 명시적으로 비동기 commit 수행
            // OffsetCommitCallback 구현체를 활용해 커밋 응답에 대한 처리를 할 수 있다.
            consumer.commitAsync(new AsyncCommitCallback());
        }
    }
}
