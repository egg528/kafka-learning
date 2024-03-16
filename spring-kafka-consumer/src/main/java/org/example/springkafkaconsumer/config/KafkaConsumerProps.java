package org.example.springkafkaconsumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("kafka")
public class KafkaConsumerProps {
    private String servers;
    private String groupId;
    private String topic;
}
