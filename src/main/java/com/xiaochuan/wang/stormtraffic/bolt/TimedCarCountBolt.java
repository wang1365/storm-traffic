package com.xiaochuan.wang.stormtraffic.bolt;

import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.windowing.TupleWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wangxiaochuan
 * @Description: 另外一种计算时间窗口内汽车数量的实现，基于storm自身的窗口功能
 * @Date: Created in 10:26 2018/1/8
 * @Modified By:
 */
public class TimedCarCountBolt extends BaseWindowedBolt {
    private static final Logger LOG = LoggerFactory.getLogger(TimedCarCountBolt.class);
    @Override
    public void execute(TupleWindow inputWindow) {
        LOG.info("Count is: {}", inputWindow.get().size());
    }
}
