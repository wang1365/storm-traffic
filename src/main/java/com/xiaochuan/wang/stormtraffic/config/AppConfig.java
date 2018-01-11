package com.xiaochuan.wang.stormtraffic.config;

import org.yaml.snakeyaml.Yaml;

import java.util.List;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 16:17 2018/1/9
 * @Modified By:
 */
public class AppConfig {
    private List<String> alertCars;
    private boolean localModeEnabled;

    public static AppConfig from(String ymlPath) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(AppConfig.class.getClassLoader().getResourceAsStream(ymlPath), AppConfig.class);
    }

    public List<String> getAlertCars() {
        return alertCars;
    }

    public void setAlertCars(List<String> alertCars) {
        this.alertCars = alertCars;
    }

    public boolean isLocalModeEnabled() {
        return localModeEnabled;
    }

    public void setLocalModeEnabled(boolean localModeEnabled) {
        this.localModeEnabled = localModeEnabled;
    }
}
