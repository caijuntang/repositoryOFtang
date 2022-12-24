package com.cooling.hydraulic.entity;


import com.cooling.hydraulic.enums.AlarmTypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="alarm_record")
public class AlarmRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="station_id")
    private Station station;

    @Column(name="alarm_type")
    private AlarmTypeEnum alarmType;

    //报警状态 1：未解除 0：已解除
    @Column(columnDefinition = "bit(1) default 1")
    private int status;

    //报警内容
    private String content;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="fix_time")
    private LocalDateTime fixTime;

    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }

    public LocalDateTime getFixTime() {
        return fixTime;
    }

    public void setFixTime(LocalDateTime fixTime) {
        this.fixTime = fixTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
