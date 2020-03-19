package com.test2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

/*//手动提交。在有些场景我们可能对消费偏移量有更精确的管理，以保证消息不被重复消费以及消息不被丢失。
 * 假设我们对拉取到的消息需要进行写入数据库处理，或者用于其他网络访问请求等等复杂的业务处理，
 * 在这种场景下，所有的业务处理完成后才认为消息被成功消费，这种场景下，我们必须手动控制偏移量的提交。

        Kafka 提供了异步提交（commitAsync）及同步提交（commitSync）两种手动提交的方式。
两者的主要区别在于同步模式下提交失败时一直尝试提交，直到遇到无法重试的情况下才会结束，同时，同步方式下消费者线程在拉取消息时会被阻塞，
直到偏移量提交操作成功或者在提交过程中发生错误。而异步方式下消费者线程不会被阻塞，可能在提交偏移量操作的结果还未返

回时就开始进行下一次的拉取操作，在提交失败时也不会尝试提交。

        实现手动提交前需要在创建消费者时关闭自动提交，即设置enable.auto.commit=false。
然后在业务处理成功后调用commitAsync()或commitSync()方法手动提交偏移量。由于同步提交会阻塞线程直到提交消费偏移量执行结果返回，
而异步提交并不会等消费偏移量提交成功后再继续下一次拉取消息的操作，因此异步提交还提供了一个偏移量提交回调的方法
commitAsync(OffsetCommitCallback callback)。当提交偏移量完成后会回调OffsetCommitCallback 
接口的onComplete()方法，这样客户端根据回调结果执行不同的逻辑处理

原文链接：https://blog.csdn.net/qq_35349490/article/details/79790625
*/public class ConsumeTranstation {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.220.128:9092");
		props.put("group.id", "test");
//		props.put("client.id", "test");
		props.put("fetch.max.bytes", 1024);// 为了便于测试，这里设置一次fetch 请求取得的数据最大值为1KB,默认是5MB
		props.put("enable.auto.commit", false);// 设置手动提交偏移量
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		// 订阅主题
		consumer.subscribe(Arrays.asList("test1"));
//本方案的缺点是必须保证所有数据被处理后，才提交topic的offset。为避免数据的重复消费，可以用第三种方案，
		//根据每个partition的数据消费情况进行提交。
//		try {
//			int minCommitSize = 10;// 最少处理10 条消息后才进行提交
//			int icount = 0;// 消息计算器
//			while (true) {
//				// 等待拉取消息
//				ConsumerRecords<String, String> records = consumer.poll(1000);
//				for (ConsumerRecord<String, String> record : records) {
//					// 简单打印出消息内容,模拟业务处理
//					System.out.printf("partition = %d, offset = %d,key= %s value = %s%n", record.partition(),
//							record.offset(), record.key(), record.value());
//					icount++;
//				}
//				// 在业务逻辑处理成功后提交偏移量
//				if (icount >= minCommitSize) {
//					consumer.commitAsync(new OffsetCommitCallback() {
//						@Override
//						public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//							if (null == exception) {
//								// TODO 表示偏移量成功提交
//								System.out.println("提交成功");
//							} else {
//								// TODO 表示提交偏移量发生了异常，根据业务进行相关处理
//
//								System.out.println("发生了异常");
//							}
//						}
//					});
//					icount = 0; // 重置计数器
//				}
//			}
//		} catch (Exception e) {
//			// TODO 异常处理
//			e.printStackTrace();
//		} finally {
//			consumer.close();
//		}

		//推荐使用 手动提交topic中每一个partition的offset：
		//consumer.subscribe(Arrays.asList("name1", "name2"));
		
		boolean running = true;
		try {
		    while (running) {
		        ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
		        for (TopicPartition partition : records.partitions()) {
		            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
		            for (ConsumerRecord<String, String> record : partitionRecords) {
		             System.out.println(record.offset() + " : " + record.value());
		            }
		            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
		            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
		        }
		    }
		} finally {
		    consumer.close();
		}
		
	}
}

//kafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//consumer.subscribe(Arrays.asList("name1", "name2"));
//final int minBatchSize = 200;
//List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
//while (true) {
//    ConsumerRecords<String, String> records = consumer.poll(100);
//    for (ConsumerRecord<String, String> record : records) {
//        buffer.add(record);
//    }
//
//    if (buffer.size() >= minBatchSize) {
//        // operation to handle data
//        consumer.commitSync();
//        buffer.clear();
//    }
//
//————————————————不推荐
//版权声明：本文为CSDN博主「冰 河」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/l1028386804/article/details/98381877


//推荐使用 手动提交topic中每一个partition的offset：
//consumer.subscribe(Arrays.asList("name1", "name2"));
//
//boolean running = true;
//try {
//    while (running) {
//        ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
//        for (TopicPartition partition : records.partitions()) {
//            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
//            for (ConsumerRecord<String, String> record : partitionRecords) {
//                logger.info(record.offset() + " : " + record.value());
//            }
//            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
//            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
//        }
//    }
//} finally {
//    consumer.close();
//}
//————————————————
//版权声明：本文为CSDN博主「冰 河」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/l1028386804/article/details/98381877