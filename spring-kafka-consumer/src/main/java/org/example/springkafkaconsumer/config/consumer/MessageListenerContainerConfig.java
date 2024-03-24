package org.example.springkafkaconsumer.config.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.time.Duration;

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
//        // Message Listener을 할당해야 한다.
//        // 참고롤 Message Listener의 종류는 8가지가 있다. (https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages/message-listeners.html)
//        factory.setupMessageListener((MessageListener<String, String>) data -> System.out.println(data));
//        return factory;
//    }

    // multi-thread consume이 가능하도록 1개 이상의 KafkaMessageListenerContainer에게 message consume을 위임(delegate)한다.
    // Thread 개수만큼의 partition을 동시에 처리할 수 있게 된다.
    // 1개의 Thread는 한번에 1개의 partition으로부터 poll()을 할 수 있고 데이터를 처리한다.
    // 때문에 여기서 설정하는 concurrency는 1개의 partition에서 가져온 message들을 동시 처리하는 것이 아님
    // Bean 네이밍 기반 의존성 주입
    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // poll()을 통해 가져온 데이터를 Listener가 읽는 방식 Batch / Single
        // Batch Listener를 사용하려면 필수적으로 true 설정을 해줘야 한다.
        // 성능 차이 확인해볼 것
        factory.setBatchListener(false);
        factory.getContainerProperties()
               .setPollTimeout(Duration.ofSeconds(5).toMillis());
        // factory.setConcurrency(3); // 3개의 ConcurrentMessageListenerContainer 생성
        factory.setRecordMessageConverter(new StringJsonMessageConverter());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setCommonErrorHandler(new BlockingRetryErrorHandler());
        return factory;
    }
}
