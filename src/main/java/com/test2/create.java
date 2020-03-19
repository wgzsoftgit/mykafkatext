package com.test2;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;


//实现创建主题
public class create {

	public static void main(String[] args) {
        //  创建topic
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.220.128:9092");
        AdminClient adminClient = AdminClient.create(props);
        ArrayList<NewTopic> topics = new ArrayList<NewTopic>();
        NewTopic newTopic = new NewTopic("topic-trx", 1, (short) 1);//   创建   topic ： topic-trx
        topics.add(newTopic);
        CreateTopicsResult result = adminClient.createTopics(topics);
        try {
            result.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
