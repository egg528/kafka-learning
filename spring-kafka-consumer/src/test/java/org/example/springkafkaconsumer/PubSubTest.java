//package org.example.springkafkaconsumer;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.LongDeserializer;
//import org.apache.kafka.common.serialization.LongSerializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.kafka.support.converter.StringJsonMessageConverter;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.ContainerTestUtils;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@EmbeddedKafka
//public class PubSubTest {
//    private static final String TEST_TOPIC = "test_topic";
//    private KafkaMessageListenerContainer<Long, String> container;
//    private BlockingQueue<ConsumerRecord<Long, String>> records;
//    private KafkaTemplate<Long, String> template;
//    private StringJsonMessageConverter stringJsonMessageConverter;
//
//    @BeforeEach
//    public void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
//        // setup producer
//        Map<String, Object> producerProp = KafkaTestUtils.producerProps(embeddedKafkaBroker);
//        producerProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
//        producerProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        ProducerFactory<Long, String> pf = new DefaultKafkaProducerFactory<>(producerProp);
//
//        template = new KafkaTemplate<>(pf);
//        template.setDefaultTopic(TEST_TOPIC);
//        stringJsonMessageConverter = new StringJsonMessageConverter();
//        template.setMessageConverter(stringJsonMessageConverter);
//
//        // setup consumer
//        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker);
//        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
//        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        DefaultKafkaConsumerFactory<Long, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
//
//        ContainerProperties containerProperties = new ContainerProperties(TEST_TOPIC);
//        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
//        records  = new LinkedBlockingQueue<>();
//        container.setupMessageListener((MessageListener<Long, String>) data -> records.add(data) );
//
//        container.start();
//        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
//
//    }
//
//    @AfterEach
//    public void tearDown() {
//        container.stop();
//    }
//
//    @Test
//    public void testPublishAndSubscribeWithEmbeddedKafka() throws InterruptedException {
//        var testKey = 1L;
//        var testValue = "test";
//        var message = new TestMessage(
//                testKey,
//                testValue
//        );
//
//        template.send(MessageBuilder.withPayload(message)
//                .setHeader(KafkaHeaders.KEY, message.getKey())
//                .setHeader(KafkaHeaders.TOPIC, TEST_TOPIC)
//                .build());
//
//        ConsumerRecord<Long, String> received = records.poll(10, TimeUnit.SECONDS);
//
//        assertThat(received).isNotNull();
//        assertThat(received.topic()).isEqualTo(TEST_TOPIC);
//
//        Message<?> convertedMessage = stringJsonMessageConverter.toMessage(received, null, null, TestMessage.class);
//
//        assertThat(convertedMessage).isNotNull();
//        assertThat(convertedMessage.getPayload()).isNotNull();
//
//        var consumeMessage = convertedMessage.getPayload();
//
//        assertThat(consumeMessage).isEqualTo(message);
//    }
//}
