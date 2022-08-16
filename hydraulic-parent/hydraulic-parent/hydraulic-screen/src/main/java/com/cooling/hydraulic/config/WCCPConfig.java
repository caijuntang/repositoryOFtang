package com.cooling.hydraulic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wccp")
public class WCCPConfig {

    //企业ID
    public static Integer agentId=1000002;
    //部门Id
    public static String corpId="wwa1d74de552322ccc";
    //应用密钥
    public static String agentSecret="y3Xn3XE1C6dZuOj3e4G-UoRdzfi6S3FV558iz3lIsh4";

    @Value("1000002")
    public static void setAgentId(Integer agentId) {
        WCCPConfig.agentId = agentId;
    }

    @Value("wwa1d74de552322ccc")
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Value("y3Xn3XE1C6dZuOj3e4G-UoRdzfi6S3FV558iz3lIsh4")
    public void setAgentSecret(String agentSecret) {
        this.agentSecret = agentSecret;
    }

}
