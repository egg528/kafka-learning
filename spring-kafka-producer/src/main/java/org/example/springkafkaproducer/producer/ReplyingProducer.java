package org.example.springkafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class ReplyingProducer implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(ReplyingProducer.class);

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        ProducerRecord<String, String> record = ProducerRecordCreator.create("test", "key", "value");
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
        try {
            ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
            logger.info("ConsumerRecord: {}", consumerRecord.value());
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
