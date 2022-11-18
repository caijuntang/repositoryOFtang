package com.cooling.hydraulic.request.qyRequest;

import com.cooling.hydraulic.model.PumpDataForm;

public class PumpDataRequest {

    private Integer stationId;

    private PumpDataForm pumpData;

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public PumpDataForm getPumpData() {
        return pumpData;
    }

    public void setPumpData(PumpDataForm pumpData) {
        this.pumpData = pumpData;
    }
}
