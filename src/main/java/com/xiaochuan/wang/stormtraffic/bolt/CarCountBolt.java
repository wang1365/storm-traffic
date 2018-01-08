package com.xiaochuan.wang.stormtraffic.bolt;

import com.xiaochuan.wang.stormtraffic.traffic.TrafficRecord;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author: wangxiaochuan
 * @Description: 按照时间间隔统计汽车数量
 *  该实现仅适用于bolt并发度为1的场景，当并发度大于1时，统计的仅是该job内接受的消息的数量，
 *  没有进行合并，如果要计算全局数量，可借助于redis等第三方组件实现，或者使用package的另
 *  外一个类：TimedCarCountBolt
 * @Date: Created in 9:45 2018/1/5
 * @Modified By:
 */
public class CarCountBolt implements IBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(CarCountBolt.class);
    private TopologyContext context;
    private Map stormConf;

    // 统计时间窗口，例如3600，即统计每小时汽车数量
    private long intervalMs;
    private long startTime = System.currentTimeMillis();
    private long count;

    public CarCountBolt(long intervalSeconds) {
        this.context = context;
        this.intervalMs = intervalSeconds * 1000L;
    }

    public void prepare(Map stormConf, TopologyContext context) {
        this.stormConf = stormConf;
        this.context = context;
    }

    // 由于使用了IBasicBolt，不需要手动ACK，如果使用IRichBolt，请手动ACK确认消息处理成功
    public void execute(Tuple input, BasicOutputCollector collector) {
        TrafficRecord record = (TrafficRecord)input.getValueByField(TrafficRecord.getFIELD());
        LOG.info("{}: {}", count, record.getCarPlate());

        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime > intervalMs) {
            LOG.info("Total count is {} in current time window", count);
            count = 0;
            startTime = currentTime;
        }
        count++;
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
