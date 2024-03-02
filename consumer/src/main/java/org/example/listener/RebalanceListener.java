package org.example.listener;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// ConsumerRebalanceListener는 리밸런스 발생을 감지하기 위한 인터페이스
public class RebalanceListener implements ConsumerRebalanceListener {
    public Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(RebalanceListener.class);
    private final KafkaConsumer<String, String> consumer;

    public RebalanceListener(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public void updateCurrentOffset(Map<TopicPartition, OffsetAndMetadata> updatedCurrentOffset) {
        currentOffset = updatedCurrentOffset;
    }

    @Override // 리밸런스가 끝난 뒤 파티션 할당이 완료되고 호출되는 메서드
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        logger.warn("Partitions are assigned: " + partitions.toString());
    }

    @Override // 리밸런스가 시작되기 직전에 호출되는 메서드
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        logger.warn("Partitions are revoked : " + partitions.toString());
        consumer.commitSync(currentOffset);
    }
}
