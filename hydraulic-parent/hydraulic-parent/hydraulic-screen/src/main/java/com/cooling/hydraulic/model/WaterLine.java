package com.cooling.hydraulic.model;


public class WaterLine {
    private  Double insideVal;
    private  Double outsideVal;
    private  Double foreVal;

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



    public WaterLine(Double insideVal, Double outsideVal, Double foreVal) {
        this.insideVal = insideVal;
        this.outsideVal = outsideVal;
        this.foreVal = foreVal;
    }
}
