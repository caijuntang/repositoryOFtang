package com.cooling.hydraulic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wc")
public class WCConfig {

    @Value("${app_id:wx9a636b2936416367}")
    public static String appId;
    //应用密钥
    @Value("${app_secret:74a4936aa3e72a99ba2570a2f5fc282a}")
    public static String appSecret;

    @Value("${token:87QmEc1tbwWtL945jqFpExZw1yUtwLjy}")
    public static String token;

    @Value("${aes_key:PdQOpQQVmghwnK5hlG0t1k7KC61FaB3kWxEP8RnSFqb}")
    public static String aesKey;

    public  void setAppId(String appId) {
        WCConfig.appId = appId;
    }

    public  void setAppSecret(String appSecret) {
        WCConfig.appSecret = appSecret;
    }

    public  void setToken(String token) {
        WCConfig.token = token;
    }

    public  void setAesKey(String aesKey) {
        WCConfig.aesKey = aesKey;
    }

    public  String getAppId() {
        return appId;
    }

    public  String getAppSecret() {
        return appSecret;
    }

    public  String getToken() {
        return token;
    }

    public  String getAesKey() {
        return aesKey;
    }
}
