package com.xiaochuan.wang.stormtraffic.topology;

import com.xiaochuan.wang.stormtraffic.bolt.AlertBolt;
import com.xiaochuan.wang.stormtraffic.bolt.CarCountBolt;
import com.xiaochuan.wang.stormtraffic.spout.TrafficKafkaSpoutBuilder;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Arrays;
import java.util.List;

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
                .topic("traffic")
                .build();

        TopologyBuilder builder = new TopologyBuilder();

        // 添加kafka数据源
        String spoutName = "traffic-kafka";
        builder.setSpout(spoutName, kafkaSpout)
                .setDebug(false)
                .setNumTasks(1)
                .setMaxTaskParallelism(1);

        // 添加bolt统计车辆数, 并关联到kafka spout
        builder.setBolt(CarCountBolt.class.getSimpleName(), new CarCountBolt(3600))
                .shuffleGrouping(spoutName)
                .setDebug(false);

        // 添加告警bolt
        List<String> dangerousCars = Arrays.asList("苏A10001", "沪C10003", "浙B10002");
        List<String> mails = Arrays.asList("wangxiaochuan01@163.com");
        builder.setBolt(AlertBolt.class.getSimpleName(), new AlertBolt(dangerousCars, mails))
                .shuffleGrouping(spoutName);

        StormTopology topology = builder.createTopology();

        return topology;
    }
}
