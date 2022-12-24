package com.cooling.hydraulic.response.wechat;

import java.util.List;

public class WCUserList extends WCBaseResponse {

    private int total;
    private int count;
    private UserData data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
