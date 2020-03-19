package com.test2.shiji;



import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author : guaoran
 * @Description : <br/>
 *  生产者消息发送
 * @date :2018/11/9 16:00
 */
public class KafkaProducerDemo extends Thread {
    private final static String CONNECT_URL =
            "192.168.220.128:9092";//,192.168.45.134:9092,192.168.45.135:9092
    private final KafkaProducer<Integer,String> producer;
    private final boolean isAysnc;
    private final String topic;
    public KafkaProducerDemo(String topic,boolean isAysnc){
        this.isAysnc = isAysnc;
        this.topic = topic;
        Properties properties = new Properties();
        //连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,CONNECT_URL);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG,"KafkaProducerDemo");
        // producer 发送消息到 broker 上以后的确认值
        // 0 :表示 producer 不需要等待 broker 的消息确认，如果server 宕机 ，数据会丢失
        // 1 :表示 producer 只需要获得 kafka 集群中的 leader 节点确认即可
        // all(-1) :表示 producer 需要ISR中所有的Replica接受确认，速度较慢，安全性最高，如果只有一个Replica时，并不能一定能避免数据丢失。
        properties.put(ProducerConfig.ACKS_CONFIG,"-1");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        /*
           batch.size
           生产者发送多个消息到broker上的同一个分区时，为了减少网络请求带来的性能开销，通过批量的方式来提交消息，
           可以通过这个参数来控制批量提交的字节数大小，默认是16kb，意味着当一批消息大小达到指定的 batch.size 时会统一发送。

           linger.ms
            producer 默认会把两次发送时间间隔内收集到的所有 request 进行一次聚合，然后再发送，以提高吞吐量。
            而linger.ms 就是为每次发送到broker 的请求增加一些 delay，以此来聚合更多的message请求。

           batch.size 和 linger.ms 这两个参数是 kafka 的性能优化的关键参数，如果两个都配置，只要满足其中一个要求，就会发送消息到broker

           max.request.size
            设置请求的数据的最大字节数，为了防止发生较大的数据包影响到吞吐量，默认是1MB
         */
        producer = new KafkaProducer<Integer, String>(properties);


    }

    @Override
    public void run() {
        int num = 0;
        while(num<50){
            String message = "message_"+num;
            System.out.println("begin...send..."+message);
            if(isAysnc){//异步发送
                producer.send(new ProducerRecord<Integer, String>(topic,message),new Callback(){
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if(recordMetadata != null){
                            System.out.println("async-offset:"+recordMetadata.offset()+
                                    "->partition:"+recordMetadata.partition());
                        }
                    }
                });
            }else{//同步发送
                try {
                    RecordMetadata recordMetadata = producer.send(new ProducerRecord<Integer, String>(topic,message)).get();
                    System.out.println("sync-offset:"+recordMetadata.offset()+
                            "->partition:"+recordMetadata.partition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            num++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new KafkaProducerDemo("test1",false).start();
    }
}
