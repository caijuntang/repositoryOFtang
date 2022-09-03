package com.cooling.hydraulic.model;


public class WaterLine {
    private  Double insideVal;
    private  Double outsideVal;
    private  Double foreVal;
    private  Boolean isAlarm;

    public Double getInsideVal() {
        return insideVal;
    }

    public void setInsideVal(Double insideVal) {
        this.insideVal = insideVal;
    }

    public Double getOutsideVal() {
        return outsideVal;
    }

    public void setOutsideVal(Double outsideVal) {
        this.outsideVal = outsideVal;
    }

    public Double getForeVal() {
        return foreVal;
    }

    public void setForeVal(Double foreVal) {
        this.foreVal = foreVal;
    }

    public Boolean getAlarm() {
        return isAlarm;
    }

    public void setAlarm(Boolean alarm) {
        isAlarm = alarm;
    }


    public WaterLine(Double insideVal, Double outsideVal, Double foreVal, Boolean isAlarm) {
        this.insideVal = insideVal;
        this.outsideVal = outsideVal;
        this.foreVal = foreVal;
        this.isAlarm = isAlarm;
    }
}
