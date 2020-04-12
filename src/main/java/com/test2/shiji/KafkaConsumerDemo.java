package com.test2.shiji;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author : guaoran
 * @Description : <br/>
 *  消费端消息接受
 * @date :2018/11/9 16:00
 */
public class  KafkaConsumerDemo extends Thread {
    private final static String CONNECT_URL =
            "192.168.220.129:9092";//,192.168.45.134:9092,192.168.45.135:9092
    private final KafkaConsumer<Integer,String> consumer;
    public KafkaConsumerDemo(String topic){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,CONNECT_URL);
        //分组id---------------------------
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"KafkaConsumerDemo1");
        // enable.auto.commit 消费者消费消息以后自动提交，只有当消息提交以后，该消息才不会被再次接收到
        // 还可以配合 auto.commit.interval.ms 控制自动提交的频率。
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        //auto.offset.reset
        //这个参数是针对新的 groupid中的消费者而言的，当有新的groupid的消费者来消费指定的topic时，对于该参数的配置，会有不同的语义
        // 当 kafka 没有初始偏移量或服务器上当前偏移量不再存在时
        // latest：自动将偏移重置为最新偏移
        // earliest：自动将偏移量重置为最早的偏移量
        // none：如果没有找到消费者组的先前偏移，则向用户抛出异常
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        /*
           max.poll.records
            此设置限制每次调用poll 返回的消息数，这样可以更容易的预测每次 poll 间隔要处理的最大值，通过调整此值，可以减少poll 间隔

         */
        consumer = new KafkaConsumer<Integer, String>(properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {
        while(true){
            ConsumerRecords<Integer,String> consumerRecords = consumer.poll(100);
            for (ConsumerRecord r :consumerRecords) {
                System.out.println("consumer...receive.."+r.value());
            }
        }
    }

    public static void main(String[] args) {
        new KafkaConsumerDemo("test1").start();
    }
}
