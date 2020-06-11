package com.web.config;

import java.util.HashMap;
import java.util.Map;
 
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
 
 
 
/**
 * 
 * Description：Kafka消费者
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
    @KafkaListener(topics = { "taskCmd" })
	public void taskCmd(ConsumerRecord<?, ?> record) {
		Object message = record.value();
		logger.info("收到管理平台命令:" + message);
	}
 * 
 */
 
//@Configuration
//@EnableKafka
public class KafkaConsumer {
 
	/**
	 * 
	 * Description：获取配置
	 * Date：		2017年7月11日
	 * @author 		shaqf
	 */
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		// kafka.metadata.broker.list=10.16.0.214:9092,10.16.0.215:9092,10.16.0.216:9092
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.16.0.216:9092");// ReadConfigation.getConfigItem("kafka.metadata.broker.list")
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "Agent-Server-1.0.2");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}
 
	/** 获取工厂 */
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}
 
	/** 获取实例 */
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(3000);
		return factory;
	}
}

//https://blog.csdn.net/u010906369/java/article/details/74978595