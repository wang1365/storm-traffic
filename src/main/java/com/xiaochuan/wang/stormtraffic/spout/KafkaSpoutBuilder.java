package com.xiaochuan.wang.stormtraffic.spout;

import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;

import java.util.Collection;


public class KafkaSpoutBuilder {
    private String _brokers;
    private String _topic;

    public KafkaSpoutBuilder brokers(String v) {
        _brokers = v;
        return this;
    }

    public KafkaSpoutBuilder brokers(Collection<String> v) {
//        _brokers = String.join(",", v);
        return this;
    }

    public KafkaSpoutBuilder topic(String v) {
        _topic = v;
        return this;
    }

    public KafkaSpout create() {
        ZkHosts zkHosts = new ZkHosts(_brokers);
        SpoutConfig conf = new SpoutConfig(zkHosts, _topic, "/", "kafkaSpout");
        return new KafkaSpout(conf);
    }

    public static void main(String[] args) {
        KafkaSpout spout = new KafkaSpoutBuilder()
                .brokers("localhost:9092")
                .topic("traffic")
                .create();
    }
}
