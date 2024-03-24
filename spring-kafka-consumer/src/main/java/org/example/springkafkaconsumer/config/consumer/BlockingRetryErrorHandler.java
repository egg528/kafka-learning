package org.example.springkafkaconsumer.config.consumer;

import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

public class BlockingRetryErrorHandler extends DefaultErrorHandler {
    private static final FixedBackOff backOff = new FixedBackOff(3000L, 3);

    public BlockingRetryErrorHandler() {
        super(backOff);
    }

    public BlockingRetryErrorHandler(ConsumerRecordRecoverer recordRecoverer) {
        super(recordRecoverer, backOff);
    }
}
