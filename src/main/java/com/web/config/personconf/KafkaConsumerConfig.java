package com.web.config.personconf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;
/**
 * @Description： kafka 消费者配置类
 * @Author：panboyang
 * @date:2019/11/8 17:23

 */
//@Component
//@EnableKafka
	public class KafkaConsumerConfig {
		private static final Logger log= LoggerFactory.getLogger(KafkaConsumerConfig.class);
	   
		@Value("${kafka.consumer.group.id}")
		private String groupId;
		
		@Value("${kafka.producer.bootstrap.servers}")
		private String list;
		
		@Value("${kafka.consumer.enable.auto.commit}")
		private String commit;
		
		@Value("${kafka.consumer.auto.commit.interval.ms}")
		private String commitMs;
		
		@Value("${kafka.consumer.session.timeout.ms}")
		private String timeoutMs;
		
		@Value("${kafka.consumer.key.deserializer}")
		private String keyDeserializer;
		
		@Value("${kafka.consumer.key.deserializer}")
		private String valueDeserializer;
		
		@Value("${kafka.consumer.maxPollRecords}")
		private String maxPollRecords;
		
		 @Bean
		    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
		    kafkaListenerContainerFactory() {
		        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
		                new ConcurrentKafkaListenerContainerFactory();
		        factory.setConsumerFactory(consumerFactory());
		        factory.setConcurrency(3);
		        factory.getContainerProperties().setPollTimeout(3000);
		        //设置批量消费
		        factory.setBatchListener(true);
		        return factory;
		    }

		    @Bean
		    public ConsumerFactory<Integer, String> consumerFactory() {
		        return new DefaultKafkaConsumerFactory(consumerProperties());
		    }

		    @Bean
		    public Map<String, Object> consumerProperties() {
		        Map<String, Object> props= new HashMap<String, Object>();
		        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, list);
		        props.put(ConsumerConfig.GROUP_ID_CONFIG,  groupId);
		        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,  commit);
		        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,commitMs);
		        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, timeoutMs);
		        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
		        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
		        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords); //配置批量参数
		        return props;
		    }

//		    @Bean
//		    public KafkaConsumerListenser kafkaConsumerListener(){
//		        return new KafkaConsumerListenser();
//		    }
	}

//https://blog.csdn.net/wulianlang6162/java/article/details/103099447