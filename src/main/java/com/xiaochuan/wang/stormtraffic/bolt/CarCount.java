package com.xiaochuan.wang.stormtraffic.bolt;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 9:45 2018/1/5
 * @Modified By:
 */
public class CarCount implements IBasicBolt {
    private TopologyContext context;
    private Map stormConf;

    public void prepare(Map stormConf, TopologyContext context) {
        this.stormConf = stormConf;
        this.context = context;
    }

    // 由于使用了IBasicBolt，不需要手动ACK，如果使用IRichBolt，请手动ACK确认消息处理成功
    public void execute(Tuple input, BasicOutputCollector collector) {
        System.out.println(input.getMessageId().toString());
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
