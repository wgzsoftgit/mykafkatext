package com.test2;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.LeaderNotAvailableException;
import org.apache.kafka.common.errors.NotControllerException;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;



import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Exectionkafka {
	public static void main(String[] args) {

        //  配置信息
        Properties props = new Properties();
        //  kafka 集群 "192.168.220.128:9092,192.168.220.128:9093"
        props.put("bootstrap.servers", "192.168.220.128:9092");
        //  应答级别
        props.put("acks", "all");
        //  重试此时
        props.put("retries", 0);
        //  批量大小
        props.put("batch.size", 16384);
        //  提交延时
        props.put("linger.ms", 1);
        //  缓存
        props.put("buffer.memory", 33554432);
        //  KV的序列化类
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //   创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        //   发送十条消息  
        for(int i = 0; i < 20; i++)
        {
        	//1、异步发送如下  异步回调官方案例 （不阻塞）
            producer.send(new ProducerRecord<String, String>("test1", "消息--"+i),new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if(e ==null){
                            //正常处理逻辑
                            System.out.println("The offset of the record we just sent is: " + metadata.offset()); 
                            
                        }else{
                                
                              if(e instanceof RetriableException) {
                                 //处理不可重试异常  消息尺寸过大导致。
                                // ......
                              } else if(e instanceof SerializationException) {
                                 //处理不可重试异常  序列化失败异常  
                                // ......
                              }else if(e instanceof LeaderNotAvailableException ) {
                            	  //可重试异常    分区的Leader副本不可用，这可能是换届选举导致的瞬时的异常，重试几次就可以恢复
                              }else if (e instanceof NotControllerException) {
								//可重试异常   Controller主要是用来选择分区副本和每一个分区leader的副本信息，主要负责统一管理分区信息等，也可能是选举所致。
							}else  {
								//可重试异常   瞬时网络故障异常所致if (e instanceof NetWorkerException)
							}
                        }
                    }
                });
        	
        	

            try {
                Thread.sleep(2000);//停顿两秒  便于观察
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		//  最后记得关闭连接 
        producer.close();
    
            }
}
