package com.test2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;




import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 *方案一
 */
public class Consumer {

	 public static void main(String[] args) {
	        Properties props = new Properties();
	        props.put("bootstrap.servers", "192.168.220.128:9092");
	        props.put("group.id", "tests"); //表示Kafka消费者组的唯一标识
	        props.put("enable.auto.commit", "true"); // 显示设置偏移量自动提交
	        props.put("auto.commit.interval.ms", "1000");// 设置偏移量提交时间间隔
	        props.put("session.timeout.ms", "30000");
	        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);  // 创建消费者
	        //要消费的topic名称，由group.id为binghe作为consumer group统一进行管理
	        consumer.subscribe(Arrays.asList("test1"));//   设置我创建的topic：test1   同时可以订阅多个topic
	        while (true) {
	            ConsumerRecords<String, String> records = consumer.poll(10);
	            for (ConsumerRecord<String, String> record : records)
	            {
	                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
	                System.out.println();
	            }
	        }
	    }

}
