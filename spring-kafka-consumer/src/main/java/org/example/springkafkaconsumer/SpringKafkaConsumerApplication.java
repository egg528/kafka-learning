package org.example.springkafkaconsumer;

import org.example.springkafkaconsumer.domain.TestEvent;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

@EnableKafka
@SpringBootApplication
public class SpringKafkaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaConsumerApplication.class, args);
    }


    @Bean
    public ApplicationRunner runner(
            KafkaTemplate<String, TestEvent> producer
    ) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                producer.send("test", new TestEvent("1", "2"));
            }

        };
    }
}
