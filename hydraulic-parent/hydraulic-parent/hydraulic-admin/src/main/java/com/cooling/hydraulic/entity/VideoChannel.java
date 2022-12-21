package com.cooling.hydraulic.entity;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="video_channel")
public class VideoChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String videoName;

    @ManyToOne
    @JoinColumn(name="station_id")
    private Station station;

    //序列号
    private String serialNo;
    //通道 单通道默认为1 多通道需填写
    private String channel="1";

    @Column(columnDefinition = "bit(1) default 0")
    private Boolean isLive;

    @Column(columnDefinition = "bit(1) default 0")
    private Boolean status;

    private LocalDateTime createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }



}
