package com.test2.consum.doubles;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class ManualParTion2 {

	public static void main(String[] args) {

//		Properties props = new Properties();
//		props.put("metadata.broker.list", "localhost:9092");
//		KafkaConsumer consumer = new KafkaConsumer(props);
//
//// subscribe to some partitions of topic foo
//		TopicPartition partition0 = new TopicPartition("foo", 0);
//		TopicPartition partition1 = new TopicPartition("foo", 1);
//		TopicPartition[] partitions = new TopicPartition[2];
//		partitions[0] = partition0;
//		partitions[1] = partition1;
//		consumer.subscribe(partitions);
//
//// seek to the last committed offsets to avoid duplicates
//		Map<TopicPartition, Long> lastCommittedOffsets = getLastCommittedOffsetsFromCustomStore();
//		consumer.seek(lastCommittedOffsets);
//
//// find the offsets of the latest available messages to know where to stop consumption
//		Map<TopicPartition, Long> latestAvailableOffsets = consumer.offsetsBeforeTime(-2, partition0, partition1);
//		boolean isRunning = true;
//		Map<TopicPartition, Long> consumedOffsets = new HashMap<TopicPartition, Long>();
//		while (isRunning) {
//			Map<String, ConsumerRecords> records = consumer.poll(100, TimeUnit.MILLISECONDS);
//			Map<TopicPartition, Long> lastConsumedOffsets = process(records);
//			consumedOffsets.putAll(lastConsumedOffsets);
//			// commit offsets for partitions 0,1 for topic foo to custom store
//			commitOffsetsToCustomStore(consumedOffsets);
//			for (TopicPartition partition : partitions) {
//				if (consumedOffsets.get(partition) >= latestAvailableOffsets.get(partition))
//					isRunning = false;
//				else
//					isRunning = true;
//			}
//		}
//		commitOffsetsToCustomStore(consumedOffsets);
//		consumer.close();
	}
}
