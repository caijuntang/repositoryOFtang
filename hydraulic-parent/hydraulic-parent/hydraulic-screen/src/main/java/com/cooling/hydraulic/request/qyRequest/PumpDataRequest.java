package com.cooling.hydraulic.request.qyRequest;

import com.cooling.hydraulic.model.PumpDataModel;

public class PumpDataRequest {

    private Integer stationId;

    private PumpDataModel pumpData;

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public PumpDataModel getPumpData() {
        return pumpData;
    }

    public void setPumpData(PumpDataModel pumpData) {
        this.pumpData = pumpData;
    }
}
