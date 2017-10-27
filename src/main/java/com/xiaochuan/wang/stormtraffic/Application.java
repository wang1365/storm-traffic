package com.xiaochuan.wang.stormtraffic;

import com.xiaochuan.wang.stormtraffic.spout.WordCountSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

public class Application {


    public static void main(String[] args) {
        // 创建拓扑（spout & bolt）
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("wordline", new WordCountSpout());
        StormTopology topology = builder.createTopology();

        // 任务配置
        Config config = new Config();
        config.put("file", "C:\\Users\\wangxiaochuan\\Desktop\\storm-traffic\\README.md");

        // 将拓扑提交到本地集群
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("demo", config, topology);
    }
}
