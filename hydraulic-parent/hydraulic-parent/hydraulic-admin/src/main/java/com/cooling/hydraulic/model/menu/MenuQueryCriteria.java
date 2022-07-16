package com.cooling.hydraulic.model.menu;

import java.sql.Timestamp;
import java.util.List;

public class MenuQueryCriteria {

    private String blurry;

    private List<Timestamp> createTime;

    private Boolean pidIsNull;

    private Long pid;

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }

    public List<Timestamp> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Timestamp> createTime) {
        this.createTime = createTime;
    }

    public Boolean getPidIsNull() {
        return pidIsNull;
    }

    public void setPidIsNull(Boolean pidIsNull) {
        this.pidIsNull = pidIsNull;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
