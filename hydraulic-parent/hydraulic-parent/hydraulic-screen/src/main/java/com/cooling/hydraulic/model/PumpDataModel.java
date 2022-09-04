package com.cooling.hydraulic.model;

public class PumpDataModel {
    private Integer pumpNo;
    private Double va=0.00;
    private Double vb=0.00;
    private Double vc=0.00;
    private Double aa=0.00;
    private Double ab=0.00;
    private Double ac=0.00;

    public Integer getPumpNo() {
        return pumpNo;
    }

    public void setPumpNo(Integer pumpNo) {
        this.pumpNo = pumpNo;
    }

    public Double getVa() {
        return va;
    }

    public void setVa(Double va) {
        this.va = null == va ? 0.00 : va;
    }

    public Double getVb() {
        return vb;
    }

    public void setVb(Double vb) {
        this.vb = null == vb ? 0.00 : vb;
    }

    public Double getVc() {
        return vc;
    }

    public void setVc(Double vc) {
        this.vc = null == vc ? 0.00 : vc;
    }

    public Double getAa() {
        return aa;
    }

    public void setAa(Double aa) {
        this.aa = null == aa ? 0.00 : aa;
        ;
    }

    public Double getAb() {
        return ab;
    }

    public void setAb(Double ab) {
        this.ab = null == ab ? 0.00 : ab;
        ;
    }

    public Double getAc() {
        return ac;
    }

    public void setAc(Double ac) {
        this.ac = null == ac ? 0.00 : ac;
    }

    @Override
    public String toString() {
        return "PumpDataModel{" +
                "pumpNo=" + pumpNo +
                ", va=" + va +
                ", vb=" + vb +
                ", vc=" + vc +
                ", aa=" + aa +
                ", ab=" + ab +
                ", ac=" + ac +
                '}';
    }
}
