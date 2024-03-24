package org.example.springkafkaconsumer.config.producer;

import org.example.springkafkaconsumer.domain.TestEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaTemplateConfig {
    @Bean
    public KafkaTemplate<String, TestEvent> kafkaTemplate(ProducerFactory<String, TestEvent> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
}
