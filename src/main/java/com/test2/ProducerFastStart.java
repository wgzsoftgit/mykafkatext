package com.test2;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
 
import java.util.Properties;
 
public class ProducerFastStart {
 
    public static final String BROKER_LIST = "192.168.220.128:9092";
    public static final String TOPIC = "test1";
 
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        //构建所需要发送的消息
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "Hello Kafka!");
        //配置生产者客户端参数并创建KafkaProducer示例
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            //发送消息
            producer.send(record, (metadata, exception) -> {
            	//处理异常情况
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println(metadata.topic() + "-" + metadata.partition() + ":" + metadata.offset());
                }
            });
        }
    }
}
