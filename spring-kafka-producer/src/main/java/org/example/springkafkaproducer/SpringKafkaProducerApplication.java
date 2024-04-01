package org.example.springkafkaproducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.springkafkaproducer.event.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class SpringKafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaProducerApplication.class, args);
    }
}
