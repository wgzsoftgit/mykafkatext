package com.test2;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;



import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
	public static void main(String[] args) {

        //  配置信息
        Properties props = new Properties();
        //  kafka 集群 "192.168.220.128:9092,192.168.220.128:9093"
        props.put("bootstrap.servers", "192.168.220.129:9092");
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
        for(int i = 0; i < 3; i++)
        {
        	//1、异步发送如下  异步回调官方案例 （不阻塞）
//            producer.send(new ProducerRecord<String, String>("test1", "消息--"+i),new Callback() {
//                //当Kafka返回错误的时候，onCompletion方法会收到一个非null的异常。上面的例子直接打印异常消息，但是如果是生产环境，需要做一些处理错误的操作
//            	@Override
//                public void onCompletion(RecordMetadata metadata, Exception exception) {
//                    if(exception != null){
//                     exception.printStackTrace();
//                    }else {
//                    	System.out.println("The offset of the record we just sent is: " + metadata.offset());
//                    }
//                }
//            });
        	
        	
//2、同步发送官方案例 （阻塞）
        	 
				//producer.send(new ProducerRecord<String, String>("test1", "消息--"+i)).get();//1方案
				//2方案
		     		 Future<RecordMetadata> funture=	producer.send(new ProducerRecord<String, String>("test1", "消息--"+i));
		     		try {
		     			RecordMetadata  rm= funture.get();
		     			System.out.println("服务器broker响应：主题："+rm.topic()+"\t分区"+rm.partition()+"\t偏移量："+rm.offset());
					} catch (InterruptedException | ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
   
        	 


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
