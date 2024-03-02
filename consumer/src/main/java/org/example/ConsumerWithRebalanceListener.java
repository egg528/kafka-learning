package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.listener.RebalanceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// 아래 예시는 Single Thread로 동작하는 Consumer를 기준으로 작성된 예시이다.
public class ConsumerWithRebalanceListener {
    private final static Logger logger = LoggerFactory.getLogger(ConsumerWithRebalanceListener.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private final static String GROUP_ID = "test-group";

    private static KafkaConsumer<String, String> consumer;
    private static RebalanceListener rebalanceListener;

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 리밸런스 발생 시 수동 커밋을 하기 위해

        consumer = new KafkaConsumer<>(configs);
        rebalanceListener =  new RebalanceListener(consumer);

        consumer.subscribe(Arrays.asList(TOPIC_NAME), rebalanceListener); // Custom Rebalance Listener 할당

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();

            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);

                currentOffset.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1, null)
                );

                // 이렇게 처리한다고 해도 1, 2개의 데이터 중복 처리는 일어날 가능성이 있다.
                // 개별 commit은 rebalance가 일어났을 경우에만 사용해도 괜찮을 것 같다.
                rebalanceListener.updateCurrentOffset(currentOffset);

                // Rebalance Listener를 통해 리밸런싱 시 가장 최근에 처리한 record를 기준으로 commit을 한다.
                // 때문에 아래 코드가 없어도 리밸런스로 인한 대량의 데이터 중복 처리는 막을 수 있을 것이다.
                // consumer.commitSync(currentOffset);
            }

            // 평시에는 records를 기준으로 commit하는 게 효율적일듯.
            consumer.commitSync();
        }
    }
}
