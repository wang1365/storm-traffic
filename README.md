# Storm-traffic
> 使用storm接入交通数据，实时计算速度流量等交通模型

## 环境

* Window 7
* Storm 1.1.1
* Zookeeper 3.4.9
* Kafka 2.12
* Java 8
* Python 3.6(官方推荐版本2.6.6，3.x也可以工作)

## Zookeeper 安装
> Storm cluster 使用zookeeper来管理集群，同时由于kafka也使用zookeeper来进行分布式协调，
简化起见，我们对storm和kafka使用同一套zookeeper，作为开发环境，我们不考虑zookeeper
的单点故障，所以不搭建zookeeper的集群  
* 安装方法:
    * 参考我的另一个repo：[https://github.com/wang1365/zookeeper-gym]()
    * 官方文档：[https://zookeeper.apache.org/doc/trunk/zookeeperAdmin.html#sc_singleAndDevSetup]()

## Storm安装

* 下载 storm release并解压
> 安装文件：[http://storm.apache.org/downloads.html]()  
注意：不要下载源码，源码中在storm的根目录下缺少lib文件夹，storm命令无法运行。

* 环境变量设置  
    * 为了方便的使用storm命令行，在windows系统变量中新建STORM_HOME环境变量，并将storm的bin路径添加到系统PATH中
    * 注意JAVA_HOME环境变量中不能包含空格！！！
    
* Storm配置  
Storm配置文件为`%STORM_HOME%\conf\storm.yaml`, 打开该文件修改配置如下：
```yaml
# zookeeper集群地址列表，如果zookeeper不是使用的默认端口，
# 还需要配置storm.zookeeper.ports
storm.zookeeper.servers:
    - "localhost"

# nimbus所在主机名称
nimbus.host: "localhost"

# nimbus和supervisor的数据存放路径
storm.local.dir: "/storm_local"

# supervisor所在机器的所有工作进程可用的端口列表
supervisor.slots.ports:
    - 6700
    - 6701
```
* 启动storm  
生产环境中，一般zookeeper、nimbus、supervisor会分别部署在不同的主机上，简化起见，我们在同一台机器
上启动部署这些进程：
    * 启动zookeeper：`zkServer`
    * 启动nimbus：`storm numbus`
    * 启动supervisor：`storm supervisor`
    * 启动storm ui：`storm ui`

## Storm概念说明
* zookeeper  
zookeeper本身不属于Storm的范围，它是作为一个分布式协调框架帮助storm管理集群中的节点。在启动nimbus和supervisor后，
可以发现在zookeeper节点/storm/nimbuses和/storm/supervisor下分别多了对应的子节点，并且这些子节点都是临时节点。

```
[zk: localhost:2181(CONNECTED) 14] ls2 /storm/nimbuses
[HIH-D-8204.hz.ntes.domain:6627]
cZxid = 0xcb
ctime = Fri Oct 27 10:11:04 CST 2017
mZxid = 0xcb
mtime = Fri Oct 27 10:11:04 CST 2017
pZxid = 0x460
cversion = 3
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 1
numChildren = 1

[zk: localhost:2181(CONNECTED) 15] ls2 /storm/nimbuses/HIH-D-8204.hz.ntes.domain:6627
[]
cZxid = 0x460
ctime = Fri Oct 27 11:13:57 CST 2017
mZxid = 0x460
mtime = Fri Oct 27 11:13:57 CST 2017
pZxid = 0x460
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x15f5b97a5af000d
dataLength = 79
numChildren = 0
```

* nimbus  
nimbus进程部署在storm集群的主控节点上，负责在集群中分发代码，对工作节点分配任务，并监控主机故障
* supervisor
supervisor进程部署在storm集群的每一个工作节点，负责监听工作节点上已经分配的任务，管理工作进程，给工作进程
分配任务

## Kafka安装
* 下载并解压：[https://kafka.apache.org/downloads]()
* 环境变量配置，`KAFKA_HOME`以及将`%KAFKA_HOME%\bin\windows`加入到系统`PATH`
* 根据实际情况修改kafka server的配置文件：`%KAFKA_HOME%\config\server.properties`,
注意其中的zookeeper的配置如`zookeeper.connect=localhost:2181`要根据实际的情况配置。
* 启动kafka server：`kafka-server-start.bat %KAFKA_HOME%\config\server.properties`
* 测试kafka server是否正常工作
    * 启动一个consumer client：`kafka-console-consumer.bat --zookeeper localhost --topic mytopic`
    * 启动一个producer client：`kafka-console-producer.bat --broker-list localhost:9092 --topic mytopic`  
    * 在producer中发送一条消息，然后观察consumer中是否可以收到
	
### Temp