package com.cooling.hydraulic.model.alarm;

import com.cooling.hydraulic.annotation.Query;
import com.cooling.hydraulic.entity.Station;

public class AlarmQueryCriteria {

    @Query(blurry = "name",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(propName = "id",joinName = "station")
    private Integer stationId;

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
