package com.xiaochuan.wang.stormtraffic;


import com.xiaochuan.wang.stormtraffic.config.TrafficConfig;
import com.xiaochuan.wang.stormtraffic.topology.TrafficKPITopologyBuilder;
import com.xiaochuan.wang.stormtraffic.traffic.TrafficRecord;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;


import java.net.URL;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TrafficConfig trafficConfig = TrafficConfig.from("application.yml");
        boolean bLocalMode = true;

        // 创建拓扑（spout & bolt）
        StormTopology topology = TrafficKPITopologyBuilder.create(trafficConfig);

        // 拓扑相关配置
        Config conf = new Config();
        conf.put(Config.TOPOLOGY_WORKERS, 4);
        conf.put(Config.TOPOLOGY_DEBUG, false);
        // 该超时时间需要大于统计窗口的时间
        conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 300);
        conf.registerSerialization(TrafficRecord.class);

        if (bLocalMode) {
            // local模式提交，测试用
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("demo", conf, topology);
        } else {
            // 将拓扑提交到集群
            StormSubmitter.submitTopology("mytopology", conf, topology);
        }
    }

}
