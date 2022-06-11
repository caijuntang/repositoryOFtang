package com.cooling.hydraulic.context;

/**
 * 萤石云子账户
 * accesstoken 存于数据库 有效期7天 定期更新
 */
public class AccountContext {

    private String appKey;
    private String appSecret;
    private String accessToken;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
