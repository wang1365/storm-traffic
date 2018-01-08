package com.xiaochuan.wang.stormtraffic.traffic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * @author: wangxiaochuan
 * @Description:
 * @Date: Created in 16:22 2018/1/8
 * @Modified By:
 */
public class TrafficRecord {
    private static final Logger LOG = LoggerFactory.getLogger(TrafficRecord.class);

    private LocalDateTime localDateTime;
    private String carPlate;
    private float carSpeed;
    private float longitude;
    private float latitude;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public float getCarSpeed() {
        return carSpeed;
    }

    public void setCarSpeed(float carSpeed) {
        this.carSpeed = carSpeed;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public static TrafficRecord of(String raw) {
        String[] items = raw.split(",");
        if (items.length < 5) {
            return null;
        }
        TrafficRecord record = new TrafficRecord();

        try {
            record.setLocalDateTime(LocalDateTime.parse(items[0]));
            record.setCarPlate(items[1]);
            record.setCarSpeed(Float.parseFloat(items[2]));
            record.setLongitude(Float.parseFloat(items[3]));
            record.setLatitude(Float.parseFloat(items[4]));
        } catch (DateTimeParseException e) {
            LOG.error("{} parse time error, {}", raw, e.getMessage());
            record = null;
        } catch (NumberFormatException e) {
            LOG.error("{} parse error, {}", raw, e.getMessage());
            record = null;
        }

        return record;
    }
}
