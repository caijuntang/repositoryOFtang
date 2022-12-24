package com.cooling.hydraulic.requestDto.wechat;

public class UserOpenId {

    private String openid;

    public UserOpenId() {
    }

    public UserOpenId(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
