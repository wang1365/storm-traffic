package com.xiaochuan.wang.stormtraffic.topology;

import com.xiaochuan.wang.stormtraffic.bolt.CarCount;
import com.xiaochuan.wang.stormtraffic.spout.TrafficKafkaSpoutBuilder;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Arrays;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 10:07 2018/1/5
 * @Modified By:
 */
public class TrafficKPITopologyBuilder {
    public static StormTopology create() {
        KafkaSpout kafkaSpout = new TrafficKafkaSpoutBuilder()
                .brokers(Arrays.asList("nbot18.dg.163.org:9092"))
                .topic("videodata")
                .build();

        TopologyBuilder builder = new TopologyBuilder();

        // 添加kafka数据源
        String spoutName = "traffic-kafka";
        builder.setSpout(spoutName, kafkaSpout)
                .setDebug(true)
                .setNumTasks(1)
                .setMaxTaskParallelism(1);

        // 添加bolt统计车辆数, 并关联到kafka spout
        builder.setBolt(CarCount.class.getSimpleName(), new CarCount())
                .shuffleGrouping(spoutName)
                .setDebug(true);

        StormTopology topology = builder.createTopology();

        return topology;
    }
}
