package com.web;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * kafka消费者测试
 *  @KafkaListener会自动接收kafka的主题为test1的数据
 */
@Component
public class TestConsumer {
//http://localhost:8080/test1
    @KafkaListener(topics = {"test1"}, groupId = "receiver")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        System.out.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
   System.err.println("com.web.TestConsumer"+record.toString());
    }
    
    
    
    /**
     * ① id：消费者ID；

② groupId：消费组ID；

③ topics：监听的topic，可监听多个；

④ topicPartitions：可配置更加详细的监听信息，可指定topic、parition、offset监听。

上面onMessage2监听的含义：监听topic1的0号分区，同时监听topic2的0号分区和topic2的1号分区里面offset从8开始的消息。

注意：topics和topicPartitions不能同时使用；
————————————————
版权声明：本文为CSDN博主「Felix-Yuan」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/yuanlong122716/java/article/details/105160545
     * @Title 指定topic、partition、offset消费
     * @Description 同时监听topic1和topic2，监听topic1的0号分区、topic2的 "0号和1号" 分区，指向1号分区的offset初始值为8
     * @Author long.yuan
     * @Date 2020/3/22 13:38
     * @Param [record]
     * @return void
     **/
//    @KafkaListener(id = "consumer1",groupId = "felix-group",topicPartitions = {
//            @TopicPartition(topic = "topic1", partitions = { "0" }),
//            @TopicPartition(topic = "topic2", partitions = "0", partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
//    })
//    public void onMessage2(ConsumerRecord<?, ?> record) {
//        System.out.println("topic:"+record.topic()+"|partition:"+record.partition()+"|offset:"+record.offset()+"|value:"+record.value());
//    }
 //https://blog.csdn.net/yuanlong122716/java/article/details/105160545
    
    /**
     * @Payload：获取的是消息的消息体，也就是发送内容

@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY)：获取发送消息的key

@Header(KafkaHeaders.RECEIVED_PARTITION_ID)：获取当前消息是从哪个分区中监听到的

@Header(KafkaHeaders.RECEIVED_TOPIC)：获取监听的TopicName

@Header(KafkaHeaders.RECEIVED_TIMESTAMP)：获取时间戳
     * @param data
     * @param key
     * @param partition
     * @param topic
     * @param ts
     */
//    @KafkaListener(id = "params", topics = "topic.params")
//
//    public void otherListener(@Payload String data,
//
//                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
//
//                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
//
//                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
//
//                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
//
//        log.info("topic.params receive : \n"+
//
//                "data : "+data+"\n"+
//
//                "key : "+key+"\n"+
//
//                "partitionId : "+partition+"\n"+
//
//                "topic : "+topic+"\n"+
//
//                "timestamp : "+ts+"\n"
//
//        );
//
//    } 
//    
    
/**
 * 
# 设置批量消费
spring.kafka.listener.type=batch
# 批量消费每次最多消费多少条消息
spring.kafka.consumer.max-poll-records=50
 */
@KafkaListener(id = "consumer2",groupId = "felix-group", topics = "test1")
public void onMessage3(List<ConsumerRecord<?, ?>> records) {
    System.out.println("com.web.TestConsumer>>>批量消费一次，records.size()="+records.size());
    for (ConsumerRecord<?, ?> record : records) {
        System.out.println("com.web.TestConsumer批量消费"+record.value());
    }
}










//--------------------ConsumerAwareListenerErrorHandler 异常处理器-------------------
//新建一个异常处理器，用@Bean注入
//@Bean
//public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
// return (message, exception, consumer) -> {
//     System.out.println("消费异常："+message.getPayload());
//     return null;
// };
//}



//将这个异常处理器的BeanName放到@KafkaListener注解的errorHandler属性里面
@KafkaListener(topics = {"test1"},errorHandler = "consumerAwareErrorHandler")
public void onMessage4(ConsumerRecord<?, ?> record) throws Exception {
 throw new Exception("com.web.TestConsumer简单消费-模拟异常");
}

//批量消费也一样，异常处理器的message.getPayload()也可以拿到各条消息的信息
@KafkaListener(topics = "test1",errorHandler="consumerAwareErrorHandler")
public void onMessage5(List<ConsumerRecord<?, ?>> records) throws Exception {
 System.out.println("com.web.TestConsumer批量消费一次...");
 throw new Exception("com.web.TestConsumer批量消费-模拟异常");
}






}


