package com.web.file;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.IntegerDeserializer;
/**
 * 使用Ack机制确认消费
Kafka是通过最新保存偏移量进行消息消费的，而且确认消费的消息并不会立刻删除，所以我们可以重复的消费未被删除的数据，当第一条消息未被确认，而第二条消息被确认的时候，Kafka会保存第二条消息的偏移量，也就是说第一条消息再也不会被监听器所获取，除非是根据第一条消息的偏移量手动获取。Kafka的ack 机制可以有效的确保消费不被丢失。因为自动提交是在kafka拉取到数据之后就直接提交，这样很容易丢失数据，尤其是在需要事物控制的时候。

使用Kafka的Ack机制比较简单，只需简单的三步即可：

设置ENABLE_AUTO_COMMIT_CONFIG=false，禁止自动提交
设置AckMode=MANUAL_IMMEDIATE
监听方法加入Acknowledgment ack 参数
4．使用Consumer.seek方法，可以指定到某个偏移量的位置
 * @author wgz
 * @date 创建时间：2020年6月11日 下午7:57:38
 */
@Component

public class AckListener {

    private static final Logger log = LoggerFactory.getLogger(AckListener.class);



    private Map<String, Object> consumerProps() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return props;

    }



    @Bean("ackContainerFactory")

    public ConcurrentKafkaListenerContainerFactory ackContainerFactory() {

        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));

        factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));

        return factory;

    }





    @KafkaListener(id = "ack", topics = "topic.ack", containerFactory = "ackContainerFactory")

    public void ackListener(ConsumerRecord record, Acknowledgment ack) {

        log.info("topic.quick.ack receive : " + record.value());

        ack.acknowledge();

    }

}