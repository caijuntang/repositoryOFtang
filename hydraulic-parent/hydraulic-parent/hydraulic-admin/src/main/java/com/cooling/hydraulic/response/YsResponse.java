package com.cooling.hydraulic.response;

public class YsResponse {

    private AccessToken data;

    private String code;
    private String msg;

    public AccessToken getData() {
        return data;
    }

    public void setData(AccessToken data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
