kafka  已经可以搭建成功 
功能 ：
  消费者    同步和异步
  生产者     
  集成springboot  
  
  
  1、配置 vi server.properties 
  broker.id=1

# 监听修改成本机ip ,ifconfig的ip地址

listeners=PLAINTEXT://192.168.45.135:9092

## 对外的ip地址

advertised.listeners=PLAINTEXT://192.168.45.135:9092

# advertised.host.name=192.168.45.135

# advertised.port=9092

# 修改 kafka 日志 目录，可以采用默认，不过会丢失

log.dirs=/guaoran/kafka/logs

# 配置zookeeper 的集群地址

zookeeper.connect=192.168.45.131:2181,192.168.45.134:2181,192.168.45.135:2181

启动 zookeeper   
./zkServer.sh start
启动 kafka
bin/kafka-server-start.sh  config/server.properties &



