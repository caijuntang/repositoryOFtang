package com.cooling.hydraulic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wc")
public class WCConfig {
    //部门Id
    @Value("${app_id:wx9a636b2936416367}")
    public static String appId;
    //应用密钥
    @Value("${app_secret:74a4936aa3e72a99ba2570a2f5fc282a}")
    public static String appSecret;

    public  void setAppId(String appId) {
        WCConfig.appId = appId;
    }

    public void setAppSecret(String appSecret) {
        WCConfig.appSecret = appSecret;
    }
}
