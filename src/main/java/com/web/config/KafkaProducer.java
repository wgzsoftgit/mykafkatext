package com.web.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
 
 
/**
 * 
 * Description：Kafka生产者
 * Date：		2017年7月11日
 * 
 * 依赖：
 		<dependency>
		    <groupId>org.springframework.kafka</groupId>
		    <artifactId>spring-kafka</artifactId>
		    <version>1.0.5.RELEASE</version>
		</dependency>
 * 
 * 使用案例：
   	@Resource
	private KafkaTemplate kafkaTemplate;
	
	调用方法发送数据：
	kafkaTemplate.send(topic, msg);
 * 
 */
//@Configuration
//@EnableKafka
public class KafkaProducer {
 
	// 创建一个名为testtopic的Topic并设置分区数为8，分区副本数为2
//    @Bean
//    public NewTopic initialTopic() {
//        return new NewTopic("testtopic",8, (short) 2 );
//    }
//https://blog.csdn.net/yuanlong122716/java/article/details/105160545
    // 如果要修改分区数，只需修改配置值重启项目即可
    // 修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小
//    @Bean
//    public NewTopic updateTopic() {
//        return new NewTopic("testtopic",10, (short) 2 );
//    }

	/**
	 * 
	 * Description：获取配置
	 * Date：		2017年7月11日
	 * @author 		shaqf
	 */
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		//kafka.metadata.broker.list=10.16.0.214:9092,10.16.0.215:9092,10.16.0.216:9092
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.16.0.214:9092" );//ReadCnfigation.getConfigItem("kafka.metadata.broker.list")
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}
 
	/** 获取工厂 */
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
 
	/** 注册实例 */
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}//https://blog.csdn.net/u010906369/java/article/details/74978595