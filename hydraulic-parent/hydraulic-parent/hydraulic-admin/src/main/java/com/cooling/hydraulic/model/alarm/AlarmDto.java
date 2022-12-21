package com.cooling.hydraulic.model.alarm;

import com.cooling.hydraulic.entity.Station;

import java.time.LocalDateTime;
import java.util.List;

public class AlarmDto {

    private Integer id;

    private String stationName;

    private Integer stationId;

    private String name;

    private Double alarmLine;

    private List<String> receivers;

    private boolean status;

    private int frequency;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public boolean isStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAlarmLine() {
        return alarmLine;
    }

    public void setAlarmLine(Double alarmLine) {
        this.alarmLine = alarmLine;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
