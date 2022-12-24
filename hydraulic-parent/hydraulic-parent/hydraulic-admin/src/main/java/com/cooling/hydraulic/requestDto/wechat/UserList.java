package com.cooling.hydraulic.requestDto.wechat;

import java.util.List;

public class UserList {
    private List<UserOpenId> user_list;

    public List<UserOpenId> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<UserOpenId> user_list) {
        this.user_list = user_list;
    }
}
