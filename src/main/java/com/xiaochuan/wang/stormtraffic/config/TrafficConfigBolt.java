package com.xiaochuan.wang.stormtraffic.config;

import org.apache.storm.jdbc.bolt.JdbcLookupBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcLookupMapper;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 17:39 2018/1/11
 * @Modified By:
 */
public class TrafficConfigBolt extends JdbcLookupBolt {
    public TrafficConfigBolt(ConnectionProvider connectionProvider, String selectQuery, JdbcLookupMapper jdbcLookupMapper) {
        super(connectionProvider, selectQuery, jdbcLookupMapper);
    }

    public TrafficConfigBolt(DbConfig config) {
        super(config.getConnectionProvider(), "select * from alert", config.getJdbcLookupMapper());
        this.withQueryTimeoutSecs(30);
    }
}
