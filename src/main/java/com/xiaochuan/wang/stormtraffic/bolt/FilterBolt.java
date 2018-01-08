package com.xiaochuan.wang.stormtraffic.bolt;

import com.xiaochuan.wang.stormtraffic.traffic.TrafficRecord;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author: wangxiaochuan
 * @Description: 交通数据清洗，过滤掉无效的数据
 * @Date: Created in 11:47 2018/1/5
 * @Modified By:
 */
public class FilterBolt implements IBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(FilterBolt.class);
    private String[] patterns;

    public FilterBolt(String...patterns) {
        this.patterns = patterns;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        TrafficRecord record = TrafficRecord.of(input.getStringByField("value"));
        if (record != null && record.getCarPlate() != null) {
            boolean checkResult = false;
            for (String pattern : patterns) {
                if (record.getCarPlate().contains(pattern)) {
                    checkResult = true;
                    break;
                }
            }

            if (checkResult) {
                collector.emit(new Values(record));
            } else {
                LOG.info("Ignored car: {}", record.getCarPlate());
            }
        } else {
            LOG.info("Invalid record:", record.getRaw());
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(TrafficRecord.getFIELD()));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
