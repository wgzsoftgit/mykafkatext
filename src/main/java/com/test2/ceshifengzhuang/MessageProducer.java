package com.test2.ceshifengzhuang;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.test2.Producer;
/**
 * 发送并忘记（不关心消息是否正常到达）
 * 同步发送（等待返回Future对象）
 * 异步发送（指定回调函数，服务器在返回响应时调用该函数）
 * @author wgz
 * @date 创建时间：2020年6月10日 下午5:22:51
 */
public class MessageProducer {

    private static Properties kafkaProps;
    private static KafkaProducer<String, String> kafkaProducer;

    static{
        kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "192.168.220.129:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer<String, String>(kafkaProps);
    }

    /**
     * 一、发送并忘记（不关心消息是否正常到达）
     * @param producerRecord
     */
    public void sendMsgAndForget(ProducerRecord<String, String> producerRecord){
        kafkaProducer.send(producerRecord);
    }

    /**
     * 二、同步发送（等待返回Future对象）
     * @param producerRecord
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public RecordMetadata sendSynMsg(ProducerRecord<String, String> producerRecord) throws ExecutionException, InterruptedException {
        RecordMetadata metaData = kafkaProducer.send(producerRecord).get();
        return metaData;
    }

    /**
     * 三、异步发送（指定回调函数，服务器在返回响应时调用该函数）   推荐
     * @param producerRecord
     */
    public void sendAsynMsg(ProducerRecord<String, String> producerRecord){
        kafkaProducer.send(producerRecord, new ProducerCallback());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
    	 MessageProducer messageProducer = new MessageProducer();
    	 ProducerRecord<String, String> record = null;
//    	 record = new ProducerRecord<String, String>("test1", "smaf", "send and forget");
//         messageProducer.sendMsgAndForget(record);
    	 
    	 
//    	 record = new ProducerRecord<String, String>("test1", "send", "send");
//         try {
//             messageProducer.sendSynMsg(record);//同步提交
//         } catch (ExecutionException e) {
//             e.printStackTrace();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
         
         record = new ProducerRecord<String, String>("test1", "sendAsyn", "send asyn");
         messageProducer.sendAsynMsg(record);//异步提交
    }
    public void test() {
    	 MessageProducer messageProducer = new MessageProducer();
         
         ExecutorService executorService = Executors.newFixedThreadPool(10);
         for(int i=0; i < 3; i++){
             executorService.submit(new Runnable() {
                 @Override
                 public void run() {
                     while(true){
                         Random random = new Random();
                         int randNum = random.nextInt(3)%3 + 1;
                         ProducerRecord<String, String> record = null;
                         switch (randNum){
                             case 1 :
                                  record = new ProducerRecord<String, String>("test1", "smaf", "send and forget");
                                 messageProducer.sendMsgAndForget(record);
                                 break;
                             case 2 :
                                 record = new ProducerRecord<String, String>("test1", "send", "send");
                                 try {
                                     messageProducer.sendSynMsg(record);
                                 } catch (ExecutionException e) {
                                     e.printStackTrace();
                                 } catch (InterruptedException e) {
                                     e.printStackTrace();
                                 }
                                 break;
                             case 3:
                                 record = new ProducerRecord<String, String>("test1", "sendAsyn", "send asyn");
                                 messageProducer.sendAsynMsg(record);
                                 break;
                         }
                         try {
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                 }
             });
         }
    }
}

/**
 * 回调处理类
 */
class ProducerCallback implements Callback {
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        //回调处理逻辑
        if(null != e){
            e.printStackTrace();
        }else {
        	System.out.println("The offset of the record we just sent is: " + recordMetadata.offset());
        }
    }
}