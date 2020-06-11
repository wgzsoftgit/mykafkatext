package com.test2.ceshifengzhuang;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.test2.consum.doubles.Consumer;

/**
 * 一直循环获取 topic接收的数据
 * @author wgz
 * @date 创建时间：2020年6月10日 下午5:07:00
 */
public class MessageConsumer {
    private static Properties kafkaProps;
    private static KafkaConsumer<String, String> kafkaConsumer;

    static{
        kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "192.168.220.129:9092");
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("group.id", "testGroup");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<String, String>(kafkaProps);
    }

    public void consumeMessage(String topic) throws InterruptedException {
   //创建消费者实例, 并且订阅topic    
    	kafkaConsumer.subscribe(Collections.singletonList(topic));  //   设置我创建
        Duration duration = Duration.ofSeconds(10l);//java时间方法
        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
            System.err.println("new messages:");
            if(records.count()==0) System.err.println("empty");
            for(ConsumerRecord<String, String> record : records){
                System.err.printf("topic=%s,partition=%s,key=%s,value=%s\n",record.topic(), record.partition(), record.key(), record.value());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MessageConsumer messageConsumer = new MessageConsumer();
        messageConsumer.consumeMessage("test1");
    }
}