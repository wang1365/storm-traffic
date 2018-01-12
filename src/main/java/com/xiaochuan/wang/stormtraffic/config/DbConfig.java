package com.xiaochuan.wang.stormtraffic.config;

import com.google.common.collect.Maps;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 17:50 2018/1/11
 * @Modified By:
 */
public class DbConfig {
    private AppConfig config;
    private ConnectionProvider connectionProvider;
    private JdbcMapper jdbcMapper;
    public DbConfig(AppConfig config) {
        this.config = config;

        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://localhost:3306/test");
        hikariConfigMap.put("dataSource.user","tester");
        hikariConfigMap.put("dataSource.password","password");

        this.connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);

        List<Column> columns = Arrays.asList(
                new Column("time", Types.TIMESTAMP),
                new Column("count", Types.BIGINT));
        this.jdbcMapper = new SimpleJdbcMapper(columns);
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public JdbcMapper getJdbcMapper() {
        return jdbcMapper;
    }
}
