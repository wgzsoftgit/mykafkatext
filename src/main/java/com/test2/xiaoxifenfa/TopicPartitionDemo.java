package com.test2.xiaoxifenfa;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;




/**
 * @author : guaoran
 * @Description : <br/>
 *  自定义消息分区算法
 * @date :2019/1/15 13:35
 */
public class TopicPartitionDemo implements Partitioner {
    private final Random random = new Random();
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfoList = cluster.partitionsForTopic(topic);
        //指定发送的分区值
        int partitionNum = 0;
        if(key==null){
            // 随机分区
            partitionNum = random.nextInt(partitionInfoList.size());
        }else{
            Math.abs((key.hashCode())%partitionInfoList.size());
        }
        System.err.println("topic="+topic+",key="+key+",value="+value+",partitionNum="+partitionNum);
        return partitionNum;
    }
	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
