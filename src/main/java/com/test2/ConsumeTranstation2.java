package com.test2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
//以时间戳查询消息
/*Kafka 在0.10.1.1 版本增加了时间戳索引文件，因此我们除了直接根据偏移量索引文件查询消息之外，
 * 还可以根据时间戳来访问消息。consumer-API 
 * 提供了一个offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch)方法，
 * 该方法入参为一个Map 对象，Key 为待查询的分区，Value 为待查询的时间戳，该方法会返回时间戳大于等于待查询时间的第一条消息对应的偏移量和时间戳。
 * 需要注意的是，若待查询的分区不存在，则该方法会被一直阻塞。

        假设我们希望从某个时间段开始消费，那们就可以用offsetsForTimes()方法定位到离这个时间最近的第一条消息的偏移量，
在查到偏移量之后调用seek(TopicPartition partition, long offset)方法将消费偏移量重置到所查询的偏移量位置，然后调用poll()
方法长轮询拉取消息。例如，我们希望从主题“stock-quotation”第0 分区距离当前时间相差12 小时之前的位置开始拉取消息

原文链接：https://blog.csdn.net/qq_35349490/article/details/79790625

消费速度控制
        提供 pause(Collection<TopicPartition> partitions)和resume(Collection<TopicPartition>
partitions)方法，分别用来暂停某些分区在拉取操作时返回数据给客户端和恢复某些分区向客户端返回数据操作。通过这两个方法可以对消费速度加以控制，结合业务使用。

*/public class ConsumeTranstation2 {
	public static void main(String[] args) {

Properties props = new Properties();
props.put("bootstrap.servers", "192.168.220.129:9092");
props.put("group.id", "test");
//props.put("client.id", "test");
props.put("enable.auto.commit", true);// 显示设置偏移量自动提交
props.put("auto.commit.interval.ms", 1000);// 设置偏移量提交时间间隔
props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
// 订阅主题    和partition分区
consumer.assign(Arrays.asList(new TopicPartition("test1", 0)));
try {
    Map<TopicPartition, Long> timestampsToSearch = new HashMap<TopicPartition,Long>();
    // 构造待查询的分区
    TopicPartition partition = new TopicPartition("test1", 0);
    // 设置查询12 小时之前消息的偏移量
    timestampsToSearch.put(partition, (System.currentTimeMillis() - 12 * 3600 * 1000));
    // 会返回时间大于等于查找时间的第一个偏移量                                                       根据时间戳来访问消息
    Map<TopicPartition, OffsetAndTimestamp> offsetMap = consumer.offsetsForTimes (timestampsToSearch);
    OffsetAndTimestamp offsetTimestamp = null;
    // 这里依然用for 轮询，当然由于本例是查询的一个分区，因此也可以用if 处理
    for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : offsetMap.entrySet()) {
        // 若查询时间大于时间戳索引文件中最大记录索引时间，
        // 此时value 为空,即待查询时间点之后没有新消息生成
        offsetTimestamp = entry.getValue();
        if (null != offsetTimestamp) {
        // 重置消费起始偏移量
        consumer.seek(partition, entry.getValue().offset());
        }
    }
    while (true) {
        // 等待拉取消息
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records){
            // 简单打印出消息内容
            System.out.printf("partition = %d, offset = %d,key= %s value = %s%n", record.partition(), record.offset(), record.key(),record.value());
        }
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    consumer.close();
}

	}
}
