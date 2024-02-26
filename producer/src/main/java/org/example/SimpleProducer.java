package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.callback.CustomCallback;
import org.example.partitioner.CustomPartitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SimpleProducer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. Producer 필수 Config 설정
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 1-1. Producer 옵션 Config 설정
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class); // Custom Partitioner

        // 2. Producer 생성
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        // 3. Record 생성
        String messageValue = "test message";

        // key값 설정 X -> key값 null로 처리
        // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messageValue);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "messageKey1", messageValue);
        // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, partitionNo, "messageKey1", messageValue);

        // 4. Record 전송
        // 즉각적인 전송이 아닌 Producer 내부 버퍼에 가지고 있다가 배치 형태로 전송하게 된다
        // producer.send(record);

        // get()을 통해 보낸 데이터의 결과 동기적으로 알 수 있다.
        // RecordMetadata metadata = producer.send(record).get();
        // logger.info(metadata.toString());

        // Callback class를 활용하면 비동기적으로 결과를 알 수 있다.
        producer.send(record, new CustomCallback());

        // 5. Producer 내부 버퍼에 저장된 레코드 배치를 Broker로 전송
        producer.flush();

        // 6. Producer 인스턴스 리소스들을 종료
        producer.close();
    }
}