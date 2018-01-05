package com.xiaochuan.wang.stormtraffic;


import com.xiaochuan.wang.stormtraffic.topology.TrafficKPITopologyBuilder;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;


import java.util.Map;

public class Application {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        boolean bLocalMode = true;

        // 创建拓扑（spout & bolt）
        StormTopology topology = TrafficKPITopologyBuilder.create();

        // 拓扑相关配置
        Map<String, Object> conf = new Config();
        conf.put(Config.TOPOLOGY_WORKERS, 4);
        conf.put(Config.TOPOLOGY_DEBUG, true);

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
