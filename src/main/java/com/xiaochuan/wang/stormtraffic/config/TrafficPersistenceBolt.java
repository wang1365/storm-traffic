package com.xiaochuan.wang.stormtraffic.config;

import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;

/**
 * @author: wangxiaochuan
 * @Description: 数据库存储bolt，将统计结果插入数据库
 * @Date: Created in 17:39 2018/1/11
 * @Modified By:
 */
public class TrafficPersistenceBolt extends JdbcInsertBolt {
    public TrafficPersistenceBolt(ConnectionProvider connectionProvider, JdbcMapper jdbcMapper) {
        super(connectionProvider, jdbcMapper);
    }

    public TrafficPersistenceBolt(DbConfig config) {
        super(config.getConnectionProvider(), config.getJdbcMapper());
        this.withInsertQuery("insert into carStatistics (time, count) values (?, ?)");
    }
}
