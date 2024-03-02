package org.example.hook;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ShutdownThread.class);
    private final KafkaConsumer<String, String> consumer;

    public ShutdownThread(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public void run() {
        logger.info("Shutdown hook");
        consumer.wakeup(); // wakeup() 메서드 호출 후에는 poll() 메서드가 호출되면 WakeupException 발생
    }
}
