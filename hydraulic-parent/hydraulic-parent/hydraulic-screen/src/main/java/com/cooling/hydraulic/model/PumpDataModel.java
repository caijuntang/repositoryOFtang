package com.cooling.hydraulic.model;

import org.springframework.util.StringUtils;

public class PumpDataModel {
    private Integer pumpNo;
    private String va="0.00";
    private String vb="0.00";
    private String vc="0.00";
    private String aa="0.00";
    private String ab="0.00";
    private String ac="0.00";


    public Integer getPumpNo() {
        return pumpNo;
    }

    public void setPumpNo(Integer pumpNo) {
        this.pumpNo = pumpNo;
    }

    public String getVa() {
        return va;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public String getVb() {
        return vb;
    }

    public void setVb(String vb) {
        this.vb = StringUtils.hasText(vb)?vb : va;
    }

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = StringUtils.hasText(vc)?vc : va;
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public String getAb() {
        return ab;
    }

    public void setAb(String ab) {
        this.ab = ab;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
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
