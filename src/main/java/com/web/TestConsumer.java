package com.web;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka消费者测试
 *  @KafkaListener会自动接收kafka的主题为test1的数据
 */
@Component
public class TestConsumer {

    @KafkaListener(topics = "test1")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        System.out.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
    }
}
