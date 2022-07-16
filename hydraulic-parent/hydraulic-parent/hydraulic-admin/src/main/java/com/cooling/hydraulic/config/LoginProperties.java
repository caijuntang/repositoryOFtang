package com.cooling.hydraulic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties
public class LoginProperties {

    /**
     * 账号单用户 登录
     */
    public static boolean singleLogin = false;

    public  static String cacheKey = "USER-LOGIN-DATA";

    @Value("${login.single-login}")
    public  void setSingleLogin(boolean singleLogin) {
        this.singleLogin = singleLogin;
    }

    @Value("${login.cache.key}")
    public  void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
