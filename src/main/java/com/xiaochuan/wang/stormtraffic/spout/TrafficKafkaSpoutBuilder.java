package com.xiaochuan.wang.stormtraffic.spout;

import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;

import java.util.Arrays;
import java.util.List;

/**
 * KafkaSpout构建器
 */
public class TrafficKafkaSpoutBuilder {
    private List<String> brokers;
    private String topic;

    public TrafficKafkaSpoutBuilder brokers(List<String> v) {
        brokers = v;
        return this;
    }

    public TrafficKafkaSpoutBuilder topic(String v) {
        topic = v;
        return this;
    }

    public KafkaSpout build() {
        // 配置kafka
        // 需要设置consumer group, 注意一个partition中的消息
        // 只能被同一group中的一个consumer消费
        String allBrokers = String.join(",", brokers);
        KafkaSpoutConfig<String, String> conf = KafkaSpoutConfig
                .builder(allBrokers, topic)
                .setGroupId("wangxiaochuan-storm")
                .build();
        return new KafkaSpout(conf);
    }

    public static void main(String[] args) {
        KafkaSpout spout = new TrafficKafkaSpoutBuilder()
                .brokers(Arrays.asList("nbot18.dg.163.org:9092"))
                .topic("traffic")
                .build();
    }
}
