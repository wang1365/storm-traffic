package com.xiaochuan.wang.stormtraffic.bolt;

import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.windowing.TupleWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wangxiaochuan
 * @Description: 车流量峰值告警
 *    单位时间内容超过执行数量的汽车，发出告警通知
 * @Date: Created in 11:20 2018/1/8
 * @Modified By:
 */
public class PeakAlertBolt extends BaseWindowedBolt {
    private static final Logger LOG = LoggerFactory.getLogger(PeakAlertBolt.class);
    private final int intervalSecs;
    private final int threshold;

    /**
     * 构造函数
     * @param intervalSecs 时间窗口，单位秒
     * @param checkIntervalSecs 检测间隔，单位秒
     * @param threshold 阈值
     */
    public PeakAlertBolt(int intervalSecs, int checkIntervalSecs, int threshold) {
        super();
        this.intervalSecs = intervalSecs;
        this.threshold = threshold;
        this.withWindow(Duration.seconds(intervalSecs), Duration.seconds(checkIntervalSecs));
    }

    @Override
    public void execute(TupleWindow inputWindow) {
        int carCount = inputWindow.get().size();
        if (carCount >= this.threshold) {
            LOG.info("Alert !!! Last {} seconds, car count: {} over threshold: {} .",
                    this.intervalSecs, carCount, this.threshold);
        }
    }
}
