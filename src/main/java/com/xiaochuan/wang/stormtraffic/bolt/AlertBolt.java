package com.xiaochuan.wang.stormtraffic.bolt;

import com.xiaochuan.wang.stormtraffic.traffic.TrafficRecord;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author: wangxiaochuan
 * @Description: 告警模块，实时检测指定车辆的出现
 * @Date: Created in 11:29 2018/1/5
 * @Modified By:
 */
public class AlertBolt implements IBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(AlertBolt.class);
    private final List<String> cars;
    private final List<String> notifyMails;

    public AlertBolt(List<String> cars, List<String> notifyMails) {
        this.cars = cars;
        this.notifyMails = notifyMails;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        TrafficRecord record = (TrafficRecord)input.getValueByField(TrafficRecord.getFIELD());
        if (cars.contains(record.getCarPlate())) {
            LOG.info("Found dangerous car: {} !!!!!!!!", record.getCarPlate());
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
