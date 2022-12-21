package com.cooling.hydraulic.model.station;

import com.cooling.hydraulic.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

public class StationQueryCriteria {

    @Query(blurry = "name,nameKey,cityCode")
    private String blurry;

    @Query
    private int status=1;

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
