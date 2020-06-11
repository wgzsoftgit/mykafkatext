package com.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试kafka生产者
 */
@RestController
@RequestMapping("kafka")
public class TestKafkaProducerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
//http://localhost:8080/kafka/send?msg=%22sa%22
    @RequestMapping("send")
    public String send(String msg){
        kafkaTemplate.send("test1", msg);
        return "success";
    }
    
    
    
//------------------------带回调的生产者
//使用KafkaProducer.send(record,callback)而不是send(record)方法   
//kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理，
//有两种写法
@GetMapping("/kafka/callbackOne/{message}")
public void sendMessage2(@PathVariable("message") String callbackMessage) {
    kafkaTemplate.send("test1", callbackMessage).addCallback(success -> {
        // 消息发送到的topic
        String topic = success.getRecordMetadata().topic();
        // 消息发送到的分区
        int partition = success.getRecordMetadata().partition();
        // 消息在分区内的offset
        long offset = success.getRecordMetadata().offset();
        System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
    }, failure -> {
        System.out.println("发送消息失败:" + failure.getMessage());
    });
}
//https://blog.csdn.net/yuanlong122716/java/article/details/105160545

//@GetMapping("/kafka/callbackTwo/{message}")
//public void sendMessage3(@PathVariable("message") String callbackMessage) {
//    kafkaTemplate.send("topic1", callbackMessage).addCallback(
//    		new ListenableFutureCallback<SendResult<String, Object>>() {
//        @Override
//        public void onFailure(Throwable ex) {
//            System.out.println("发送消息失败："+ex.getMessage());
//        }
// 
//        @Override
//        public void onSuccess(SendResult<String, Object> result) {
//            System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
//                    + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
//        }
//    });
//}


@GetMapping("/kafka/transaction")
public void sendMessage7(){
    // 声明事务：后面报错消息不会发出去
    kafkaTemplate.executeInTransaction(operations -> {
        operations.send("test1","test executeInTransaction");
        throw new RuntimeException("fail");
    });
    // 不声明事务：后面报错但前面消息已经发送成功了
//   kafkaTemplate.send("test1","test executeInTransaction");
//   throw new RuntimeException("fail");
}










}
