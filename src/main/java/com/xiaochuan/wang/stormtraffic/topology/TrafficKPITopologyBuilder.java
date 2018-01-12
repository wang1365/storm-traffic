package com.xiaochuan.wang.stormtraffic.topology;

import com.xiaochuan.wang.stormtraffic.bolt.*;
import com.xiaochuan.wang.stormtraffic.config.AppConfig;
import com.xiaochuan.wang.stormtraffic.config.DbConfig;
import com.xiaochuan.wang.stormtraffic.config.TrafficPersistenceBolt;
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
    public static StormTopology create(AppConfig config) {
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



        String filterBolt = FilterBolt.class.getSimpleName();
        builder.setBolt(filterBolt, new FilterBolt("苏", "沪", "浙A"))
                .shuffleGrouping(spoutName);


        /** 添加bolt统计车辆数, 并关联到kafka spout
         注意该bolt并行度为2，即2个executor，所以单个executor中的bolt统计数量
         并不是全部的汽车数量 */
        String countBolt1 = "countBolt1";
        builder.setBolt(countBolt1, new CarCountBolt(60), 1)
                .shuffleGrouping(filterBolt)
                .setDebug(false);

        /** 添加基于时间窗口的车辆统计bolt */
        String countBolt2 = "countBolt2";
        builder.setBolt(countBolt2, new TimedCarCountBolt()
                .withTumblingWindow(BaseWindowedBolt.Duration.seconds(60)), 1)
                .shuffleGrouping(filterBolt);

        /** 添加告警bolt, 检查指定汽车是否被检测到 */
        List<String> dangerousCars = config.getAlertCars();
        List<String> mails = Arrays.asList("wangxiaochuan01@163.com");
        builder.setBolt(AlertBolt.class.getSimpleName(), new AlertBolt(dangerousCars, mails))
                .shuffleGrouping(filterBolt);

        /** 添加告警bolt, 检查单位时间内汽车数量是否达到阈值
         * 过去30秒内汽车数超过40则告警，每5秒检测一次
         */
        builder.setBolt(PeakAlertBolt.class.getSimpleName(),
                new PeakAlertBolt(30, 5, 40))
                .shuffleGrouping(filterBolt);


        String dbBoltName = "db";
        builder.setBolt(dbBoltName, new TrafficPersistenceBolt(new DbConfig(new AppConfig())))
                .setDebug(true)
        .shuffleGrouping(countBolt1);

        StormTopology topology = builder.createTopology();

        return topology;
    }
}
