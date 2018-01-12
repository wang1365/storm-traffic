package com.xiaochuan.wang.stormtraffic;


import com.xiaochuan.wang.stormtraffic.bolt.AlertBolt;
import com.xiaochuan.wang.stormtraffic.config.AppConfig;
import com.xiaochuan.wang.stormtraffic.topology.TrafficKPITopologyBuilder;
import com.xiaochuan.wang.stormtraffic.traffic.TrafficRecord;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficApplication {


    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        final Logger LOG = LoggerFactory.getLogger(AlertBolt.class);
        AppConfig trafficConfig = AppConfig.from("application.yml");

        // 创建拓扑（spout & bolt）
        StormTopology topology = TrafficKPITopologyBuilder.create(trafficConfig);

        // 拓扑相关配置
        Config conf = new Config();
        conf.put(Config.TOPOLOGY_WORKERS, 4);
        conf.put(Config.TOPOLOGY_DEBUG, false);
        conf.put(Config.TOPOLOGY_NAME, "wangxiaochuan-traffic");
        // 该超时时间需要大于统计窗口的时间
        conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 300);
        conf.registerSerialization(TrafficRecord.class);

        if (trafficConfig.isLocalModeEnabled()) {
            LOG.info("Submit task as local mode !!");
            // local模式提交，测试用
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("demo", conf, topology);
        } else {
            // 将拓扑提交到集群
            StormSubmitter.submitTopology("mytopology", conf, topology);
        }
    }

}
