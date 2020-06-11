package com.test2.ceshifengzhuang.thread.doubleKafkaConsumer;
public class ConsumerMain {

    public static void main(String[] args) {
        String brokerList = "192.168.220.129:9092";
        String groupId = "testGroup1";
        String topic = "test1";
        int consumerNum = 3;

        ConsumerGroup consumerGroup = new ConsumerGroup(consumerNum, groupId, topic, brokerList);
        consumerGroup.execute();
    }
}