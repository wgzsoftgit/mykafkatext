package com.test2.ceshifengzhuang.thread.pool;
public class Main {

    public static void main(String[] args) {
        String brokerList = "192.168.220.129:9092";//"localhost:9092,localhost:9093,localhost:9094";
        String groupId = "group2";
        String topic = "test1";
        int workerNum = 5;

        ConsumerHandler consumers = new ConsumerHandler(brokerList, groupId, topic);
        consumers.execute(workerNum);
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException ignored) {}
        consumers.shutdown();
    }
}