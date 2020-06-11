package com.test2.consum.doubles;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *方案一  同时可以订阅多个topic
 */
public class Consumer2 {

	 public static void main(String[] args) {
	        Properties props = new Properties();
	        props.put("bootstrap.servers", "192.168.220.129:9092");
	        props.put("group.id", "grtext005"); //表示Kafka消费者组的唯一标识 test
	        props.put("enable.auto.commit", "false"); // 显示设置偏移量自动提交 表示在auto.commit.interval.ms时间后会自动提交topic的offset
	        props.put("auto.commit.interval.ms", "8000");// 设置偏移量提交时间间隔 //偏移量(offset)提交频率
	        props.put("session.timeout.ms", "5000000");//默认值为5000ms
	        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	        
	      //设置使用最开始的offset偏移量为该group.id的最早。如果不设置，则会是latest即该topic最新一个消息的offset
	      //如果采用latest，消费者只能得道其启动后，生产者生产的消息
	      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");  //从头开始消费
	        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);  // 创建消费者
	        //要消费的topic名称，由group.id为binghe作为consumer group统一进行管理
	        consumer.subscribe(Arrays.asList("test1"));//   设置我创建的topic：test1   同时可以订阅多个topic
	        //自动提交topic的offset
	        List<String> list=new ArrayList<String>();
	    
	       //------------------------消费完一个分区后手动提交偏移量-----------
//	        while (true) {
//	            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
//	            for (TopicPartition partition : records.partitions()) {
//	                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
//	                for (ConsumerRecord<String, String> record : partitionRecords) {
//	                  System.err.println("now consumer the message it's offset is :"+record.offset() + " and the value is :" + record.value());
//	                }
//	                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
//	                System.err.println("now commit the partition[ "+partition.partition()+"] offset");
//	                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
//	            }
//	        }
	//https://blog.csdn.net/louisliaoxh/java/article/details/51577117
	        
	        
	        // ------------------同步提交----------------
//	        try {
//	        	 while (true) {
//	 	            ConsumerRecords<String, String> records = consumer.poll(10);
//	 	            for (ConsumerRecord<String, String> record : records)
//	 	            {
//	 	           System.out.println("offset ="+record.offset()+",# key ="+record.key()+"value ="+record.value()); 	
//	 	           //     System.err.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
//	 	                list.add(record.offset()+"");
//	 	                System.out.println();
//	 	                
//	 	            } 	      
//	 	            //	consumer.commitSync();   控制提交	 	            
//	 	        }
//			} catch (Exception e) {
//				e.printStackTrace();
//			}finally {
//				consumer.close();
//			}
//	      
	        
	        //-------------------异步提交    测试err-----------------
	        try {
	        	while (true) {
	 	            ConsumerRecords<String, String> records = consumer.poll(100);
	 	            for (ConsumerRecord<String, String> record : records)
	 	            {
	 	           System.out.println("offset ="+record.offset()+",# key ="+record.key()+"value ="+record.value()); 	
	 	            } 	      
	 	            Map<TopicPartition, OffsetAndMetadata> commitOffset=new HashMap<>();	            
	 	          commitOffset.put(new TopicPartition("my-topic1",0), new OffsetAndMetadata(1L));
	 	         
	 	          consumer.commitAsync(commitOffset, (offsets, exception)->{
	 	        	  if(exception !=null) {
	 	        		  System.err.println(offsets+"&&&"+exception);
	 	        	  }else {
	 	        		 System.err.println(offsets);
	 	        	  }
	 	          });
	        	}
	        	
	        	
			} catch (Exception e) {
				// TODO: handle exception
			}finally {
				consumer.close();
			}
	        
	        
	       // 异步和同步   --err
//	        try {
//       	 while (true) {
//	            ConsumerRecords<String, String> records = consumer.poll(100);
//	            for (ConsumerRecord<String, String> record : records)
//	            {
//	           System.err.println("offset ="+record.offset()+",# key ="+record.key()+"value ="+record.value());      
//	            } 	      
//	            	consumer.commitAsync();;   //先异步提交            
//	        }
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				consumer.commitSync(); //发生错误则同步提交
//			} finally {
//				consumer.close();//关闭消费者
//			}
//			
//		}
	        
	        
     
	        
	       
	    }

}
