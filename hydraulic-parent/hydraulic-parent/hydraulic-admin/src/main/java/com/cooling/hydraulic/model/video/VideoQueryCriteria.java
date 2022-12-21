package com.cooling.hydraulic.model.video;

import com.cooling.hydraulic.annotation.Query;
import com.cooling.hydraulic.entity.Station;

import java.time.LocalDateTime;
import java.util.List;

public class VideoQueryCriteria {

    @Query(blurry = "videoName,channel,serialNo",type = Query.Type.INNER_LIKE)
    private String blurry;

    @Query(propName = "id",joinName = "station")
    private Integer stationId;

    @Query(type = Query.Type.BETWEEN)
    private List<LocalDateTime> createTime;

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

    public List<LocalDateTime> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<LocalDateTime> createTime) {
        this.createTime = createTime;
    }
}
