package com.web.config.personconf;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
/**
 * @author: panboyang
  @date:2019/11/8 17:23
 * @description kafka 生产者配置
 */
//@Configuration
//@EnableKafka
public class KafkaProducerConfig {
	@Value("${kafka.producer.bootstrap.servers}")
	private String servers;
	
	@Value("${kafka.producer.retries}")
	private String retries;
	
	@Value("${kafka.producer.batch.size}")
	private String size;
	
	@Value("${kafka.producer.linger.ms}")
	private String ms;
	
	@Value("${kafka.producer.buffer.memory}")
	private String memory;
	
	@Value("${kafka.producer.key.serializer}")
	private String keySerializer;
	
	@Value("${kafka.producer.value.serializer}")
	private String valueSerializer;
	
	@Value("${kafka.producer.acks}")
	private String acks;
	
	@Value("${kafka.producer.defaultTopic}")
	private String defaultTopic;

	
	    public KafkaProducerConfig(){
	        System.out.println("kafka生产者配置");
	    }
	    @Bean
	    public ProducerFactory<Integer, String> producerFactory() {
	        return new DefaultKafkaProducerFactory(producerProperties());
	    }

	    @Bean
	    public Map<String, Object> producerProperties() {
	        Map<String, Object> props = new HashMap<String, Object>();
	        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
	        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
	        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,valueSerializer);
	        props.put(ProducerConfig.RETRIES_CONFIG,retries);
	        props.put(ProducerConfig.BATCH_SIZE_CONFIG,size);
	        props.put(ProducerConfig.LINGER_MS_CONFIG,ms);
	        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,memory);
	       props.put(ProducerConfig.ACKS_CONFIG,acks);
	        return props;
	    }

	    @Bean
	    public KafkaTemplate<Integer, String> kafkaTemplate() {
	        KafkaTemplate kafkaTemplate = new KafkaTemplate<Integer, String>(producerFactory(),true);
	        kafkaTemplate.setDefaultTopic(defaultTopic);
	        return kafkaTemplate;
	    }
}

//https://blog.csdn.net/wulianlang6162/java/article/details/103099447