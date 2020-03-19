package com.test2;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

public class transtationkafka {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.220.128:9092");
		props.put("transactional.id", "my-transactional-id");
		  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

	    KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		producer.initTransactions();
		 
		try {
		    producer.beginTransaction();
		    for (int i = 0; i < 10; i++)
		        producer.send(new ProducerRecord<>("test1", "key"+Integer.toString(i),"value"+ Integer.toString(i)));
		    producer.commitTransaction();
		} catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
		    // We can't recover from these exceptions, so our only option is to close the producer and exit.
		    producer.close();
		} catch (KafkaException e) {
		    // For all other exceptions, just abort the transaction and try again.
		    producer.abortTransaction();
		}
		producer.close();
	}
	
}
