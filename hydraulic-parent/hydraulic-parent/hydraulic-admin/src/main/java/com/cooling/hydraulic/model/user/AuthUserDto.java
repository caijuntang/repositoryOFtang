package com.cooling.hydraulic.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


public class AuthUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String uuid = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
