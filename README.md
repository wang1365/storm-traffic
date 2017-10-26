# storm-traffic
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
### 安装方法:
* 参考我的另一个repo：[https://github.com/wang1365/zookeeper-gym]()
* 官方文档：[https://zookeeper.apache.org/doc/trunk/zookeeperAdmin.html#sc_singleAndDevSetup]()

## Storm安装

* 下载 storm release并解压
> 安装文件：[http://storm.apache.org/downloads.html]()  
注意：不要下载源码，源码中在storm的根目录下缺少lib文件夹，storm命令无法运行。

* 环境变量设置  
    * 为了方便的使用storm命令行，在windows系统变量中新建STORM_HOME环境变量，并将storm的bin路径添加到系统PATH中
    * 注意JAVA_HOME环境变量中不能包含空格！！！
    

