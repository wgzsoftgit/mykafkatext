package com.test2.ceshifengzhuang;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.test2.Consumer;


public class MessageConsumer {
    private static Properties kafkaProps;
    private static KafkaConsumer<String, String> kafkaConsumer;

    static{
        kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "192.168.254.131:9092");
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("group.id", "testGroup");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<String, String>(kafkaProps);
    }

    public void consumeMessage(String topic) throws InterruptedException {
        kafkaConsumer.subscribe(Collections.singletonList(topic));  //   设置我创建
        Duration duration = Duration.ofSeconds(10l);//java时间方法
        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
            System.out.println("new messages:");
            if(records.count()==0) System.out.println("empty");
            for(ConsumerRecord<String, String> record : records){
                System.out.printf("topic=%s,partition=%s,key=%s,value=%s\n",record.topic(), record.partition(), record.key(), record.value());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MessageConsumer messageConsumer = new MessageConsumer();
        messageConsumer.consumeMessage("test.topic");
    }
}