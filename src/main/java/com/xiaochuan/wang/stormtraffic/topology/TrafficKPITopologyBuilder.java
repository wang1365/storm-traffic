package com.xiaochuan.wang.stormtraffic.topology;

import com.xiaochuan.wang.stormtraffic.bolt.AlertBolt;
import com.xiaochuan.wang.stormtraffic.bolt.CarCountBolt;
import com.xiaochuan.wang.stormtraffic.bolt.PeakAlertBolt;
import com.xiaochuan.wang.stormtraffic.bolt.TimedCarCountBolt;
import com.xiaochuan.wang.stormtraffic.spout.TrafficKafkaSpoutBuilder;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;

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

        /** 添加kafka数据源 */
        String spoutName = "traffic-kafka";
        builder.setSpout(spoutName, kafkaSpout)
                .setDebug(false)
                .setNumTasks(1)
                .setMaxTaskParallelism(2);

        /** 添加bolt统计车辆数, 并关联到kafka spout
         注意该bolt并行度为2，即2个executor，所以单个executor中的bolt统计数量
         并不是全部的汽车数量 */
        builder.setBolt(CarCountBolt.class.getSimpleName(), new CarCountBolt(60), 2)
                .shuffleGrouping(spoutName)
                .setDebug(false);

        /** 添加基于时间窗口的车辆统计bolt */
        builder.setBolt(TimedCarCountBolt.class.getSimpleName(), new TimedCarCountBolt()
                .withTumblingWindow(BaseWindowedBolt.Duration.seconds(60)), 1)
                .shuffleGrouping(spoutName);

        /** 添加告警bolt, 检查指定汽车是否被检测到 */
        List<String> dangerousCars = Arrays.asList("苏A10001", "沪C10003", "浙B10002");
        List<String> mails = Arrays.asList("wangxiaochuan01@163.com");
        builder.setBolt(AlertBolt.class.getSimpleName(), new AlertBolt(dangerousCars, mails))
                .shuffleGrouping(spoutName);

        /** 添加告警bolt, 检查单位时间内汽车数量是否达到阈值
         * 过去30秒内汽车数超过40则告警，每5秒检测一次
         */
        builder.setBolt(PeakAlertBolt.class.getSimpleName(),
                new PeakAlertBolt(30, 5, 40))
                .shuffleGrouping(spoutName);

        StormTopology topology = builder.createTopology();

        return topology;
    }
}
