package com.xiaochuan.wang.stormtraffic.config;

import org.yaml.snakeyaml.Yaml;

import java.util.List;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 16:17 2018/1/9
 * @Modified By:
 */
public class TrafficConfig {
    private List<String> alertCars;

    public static TrafficConfig from(String ymlPath) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(TrafficConfig.class.getClassLoader().getResourceAsStream(ymlPath), TrafficConfig.class);
    }

    public List<String> getAlertCars() {
        return alertCars;
    }

    public void setAlertCars(List<String> alertCars) {
        this.alertCars = alertCars;
    }
}
