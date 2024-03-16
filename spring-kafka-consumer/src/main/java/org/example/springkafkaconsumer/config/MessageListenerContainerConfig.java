package org.example.springkafkaconsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessageListenerContainerConfig {
//    @Bean // 토픽(의 파티션)으로부터 받는 모든 message를 single thread로 처리한다.
//    public KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainerFactory(
//            ConsumerFactory<String, String> consumerFactory) {
//        KafkaMessageListenerContainer<String, String> factory = new KafkaMessageListenerContainer<>(
//                consumerFactory,
//                // ContainerProperties 생성자는 3가지 종류가 있다. (String... topics / Pattern topicPattern / TopicPartitionOffset... topicPartitions)
//                new ContainerProperties("test")
//        );
//
//        // Message Listener을 할당해야 한다.
//        // 참고롤 Message Listener의 종류는 8가지가 있다. (https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages/message-listeners.html)
//        factory.setupMessageListener((MessageListener<String, String>) data -> System.out.println(data));
//
//        return factory;
//    }

    @Bean // multi-thread consume이 가능하도록 1개 이상의 KafkaMessageListenerContainer에게 message consume을 위임(delegate)한다.
    public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaMessageListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // poll()을 통해 가져온 데이터를 Listener가 읽는 방식 Batch / Single
        // Batch Listener를 사용하려면 필수적으로 true 설정을 해줘야 한다.
        factory.setBatchListener(true);
        factory.getContainerProperties()
                .setPollTimeout(Duration.ofSeconds(5).toMillis());

        return factory;
    }
}
