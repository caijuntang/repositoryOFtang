package com.cooling.hydraulic.model.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class UserQueryCriteria implements Serializable {

    private Long id;


    private String blurry;

    private Boolean enabled;

    private List<Timestamp> createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Timestamp> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Timestamp> createTime) {
        this.createTime = createTime;
    }
}
