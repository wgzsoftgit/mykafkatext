package com.web.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

@Component

public class BatchListener {



    private static final Logger log= LoggerFactory.getLogger(BatchListener.class);



    private Map<String, Object> consumerProps() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.220.129:9092");

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");

        //一次拉取消息数量

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,

        		StringDeserializer.class);//NumberDeserializers.IntegerDeserializer.class

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,

                StringDeserializer.class);

        return props;

    }



    @Bean("batchContainerFactory")

    public ConcurrentKafkaListenerContainerFactory listenerContainer() {

        ConcurrentKafkaListenerContainerFactory container

                = new ConcurrentKafkaListenerContainerFactory();
 //  && 同
        container.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));

        //设置并发量，小于或等于Topic的分区数

        container.setConcurrency(5);

        //必须 设置为批量监听

        container.setBatchListener(true);

        return container;

    }


//测试  --ok
    @Bean

    public NewTopic batchTopic() {

        return new NewTopic("topic.batch", 8, (short) 1);

    }
// 生产者
 //bin/kafka-console-producer.sh  --broker-list 192.168.220.129:9092  --topic topic.batch  生产者


//  消费者
//bin/kafka-console-consumer.sh --bootstrap-server 192.168.220.129:9092 --topic topic.batch --from-beginning


    @KafkaListener(id = "batchConsumer",clientIdPrefix = "batch"

            ,topics = {"topic.batch"},containerFactory = "batchContainerFactory")

    public void batchListener(List<String> data) {

        log.info("topic.batch  receive : ");
        for (String s : data) {

            log.info(  s);

        }

    }

}