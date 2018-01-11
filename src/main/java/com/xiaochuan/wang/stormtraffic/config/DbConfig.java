package com.xiaochuan.wang.stormtraffic.config;

import com.google.common.collect.Maps;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcLookupMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcLookupMapper;
import org.apache.storm.tuple.Fields;

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
    private JdbcLookupMapper jdbcLookupMapper;
    public DbConfig(AppConfig config) {
        this.config = config;

        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://localhost/test");
        hikariConfigMap.put("dataSource.user","root");
        hikariConfigMap.put("dataSource.password","password");

        this.connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);

        Fields fields = new Fields("plate", "fromtime", "totime");
        List<Column> columns = Arrays.asList(
                new Column("plate", 1),
                new Column("fromtime", 2),
                new Column("totime", 3));
        this.jdbcLookupMapper = new SimpleJdbcLookupMapper(fields, columns);
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public JdbcLookupMapper getJdbcLookupMapper() {
        return jdbcLookupMapper;
    }
}
