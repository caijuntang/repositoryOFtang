package com.cooling.hydraulic.entity;

import java.util.Date;

public class PumpUpkeepRecord {

    private Integer id;

    private PumpInfo pump;

    private Date createTime;

    private String remark;

    private String upkeepPerson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PumpInfo getPump() {
        return pump;
    }

    public void setPump(PumpInfo pump) {
        this.pump = pump;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpkeepPerson() {
        return upkeepPerson;
    }

    public void setUpkeepPerson(String upkeepPerson) {
        this.upkeepPerson = upkeepPerson;
    }
}
