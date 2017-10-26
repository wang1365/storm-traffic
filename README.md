# storm-traffic
> 使用storm接入交通数据，实时计算速度流量等交通模型

## 环境

* Window 7
* Storm 1.0.5
* Zookeeper 3.4.9
* Kafka
* Java 8
* Python 3.6(官方推荐版本2.6.6，3.x也可以工作)

## Zookeeper 安装
> Storm cluster 使用zookeeper来管理集群，同时由于kafka也使用zookeeper来进行分布式协调，
简化起见，我们对storm和kafka使用同一套zookeeper，作为开发环境，我们不考虑zookeeper
的单点故障，所以不搭建zookeeper的集群。
