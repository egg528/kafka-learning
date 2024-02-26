package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class SyncCommitConsumer {
    private final static Logger logger = LoggerFactory.getLogger(SyncCommitConsumer.class);
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

            // poll()로 받은 가장 마지막 레코드 offset을 기준으로 커밋한다.
            // 때문에 poll로 받은 데이터 모두 처리 후 commitSync() 호출
            consumer.commitSync();
        }
    }
}
