package com.cooling.hydraulic.entity;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.Date;

public class PumpUpkeepRecord {

    private Integer id;

    private PumpInfo pump;

    private LocalDateTime createTime;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
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
