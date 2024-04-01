package org.example.springkafkaproducer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.springkafkaproducer.event.TestEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
public class KafkaConfig {


    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configs;
    }

    @Bean
    public ProducerFactory<String, TestEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 기본 KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, TestEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    /**
     * Topic 명을 기반으로 다른 ProducerFactory를 사용하고 싶을 때
     */
    @Bean
    public RoutingKafkaTemplate routingKafkaTemplate(GenericApplicationContext context) {
        Map<String, Object> configs = new HashMap<>(producerFactory().getConfigurationProperties());
        DefaultKafkaProducerFactory<Object, Object> jsonProducerFactory = new DefaultKafkaProducerFactory<>(configs);
        context.registerBean("jsonProducerFactory", DefaultKafkaProducerFactory.class, () -> jsonProducerFactory);

        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DefaultKafkaProducerFactory<Object, Object> stringProducerFactory = new DefaultKafkaProducerFactory<>(configs);
        context.registerBean("stringProducerFactory", DefaultKafkaProducerFactory.class, () -> stringProducerFactory);


        Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
        map.put(Pattern.compile("test"), jsonProducerFactory);
        map.put(Pattern.compile("test2"), stringProducerFactory);
        return new RoutingKafkaTemplate(map);
    }


    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory
    ) {
        ConcurrentMessageListenerContainer<String, String> container =
                containerFactory.createContainer("test");
        container.getContainerProperties().setGroupId("test-request-id");
        return container;
    }


    /**
     * 특정 Consumer가 데이터를 전달 받았는지 여부를 확인하고 싶을 때
     * Key, Value, 응답 타입까지 지정 필요
     * 내가 메시지를 발행하고, 발행한 메시지를 처리한 서비스의 응답 메시지를 확인할 수 있다.
     */
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
            ProducerFactory producerFactory,
            ConcurrentMessageListenerContainer<String, String> repliesContainer
    ) {
        return new ReplyingKafkaTemplate(producerFactory, repliesContainer);
    }


}
