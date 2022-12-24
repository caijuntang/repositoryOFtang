package com.cooling.hydraulic.model.alarm;

import com.cooling.hydraulic.annotation.Query;
import com.cooling.hydraulic.enums.AlarmTypeEnum;

public class AlarmRecordQueryCriteria {
    @Query(blurry = "content",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(propName = "alarmType")
    private Integer type;

    @Query
    private Integer status;

    @Query(propName = "id",joinName = "station")
    private Integer stationId;


    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
