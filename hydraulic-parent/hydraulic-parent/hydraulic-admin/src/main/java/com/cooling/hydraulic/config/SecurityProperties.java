package com.cooling.hydraulic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *@author tangcaijun
 *@date 2022/7/10
 *@desc
 *
 */
@Component
@ConfigurationProperties
public class SecurityProperties {

    /**
     * Request Headers ： Authorization
     */
    public static  String header;

    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    public static String tokenStartWith;

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    public static  String base64Secret;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    public static   long tokenValidityInSeconds;

    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    public static   String onlineKey;

    /**
     * token 续期检查
     */
    public static  long detect;

    /**
     * 续期时间
     */
    public static  long renew;

    @Value("${jwt.header}")
    public void setHeader(String header) {
       this.header = header;
    }

    @Value("${jwt.token-start-with}")
    public void setTokenStartWith(String tokenStartWith) {
        this.tokenStartWith = tokenStartWith;
    }

    @Value("${jwt.base64-secret}")
    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    @Value("${jwt.token-validity-in-seconds}")
    public  void setTokenValidityInSeconds(long tokenValidityInSeconds) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    @Value("${jwt.online-key}")
    public void setOnlineKey(String onlineKey) {
        this.onlineKey = onlineKey;
    }

    @Value("${jwt.token-detect}")
    public void setDetect(long detect) {
        this.detect = detect;
    }

    @Value("${jwt.token-renew}")
    public  void setRenew(long renew) {
        this.renew = renew;
    }
}
