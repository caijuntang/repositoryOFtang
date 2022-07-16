package com.cooling.hydraulic.model.role;

import java.sql.Timestamp;
import java.util.List;

public class RoleQueryCriteria {

    private String blurry;

    private List<Timestamp> createTime;

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
}
