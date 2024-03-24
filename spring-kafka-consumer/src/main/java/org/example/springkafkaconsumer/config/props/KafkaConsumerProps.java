package org.example.springkafkaconsumer.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("kafka.consumer")
public class KafkaConsumerProps {
    private String groupId;
    private String topic;
}
