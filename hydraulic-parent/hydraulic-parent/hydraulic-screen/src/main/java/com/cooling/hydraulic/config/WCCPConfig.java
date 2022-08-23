package com.cooling.hydraulic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wccp")
public class WCCPConfig {

    //企业ID
    @Value("${agent_id:1000002}")
    public static Integer agentId;
    //部门Id
    @Value("${corp_id:wwa1d74de552322ccc}")
    public static String corpId;
    //应用密钥
    @Value("${agent_secret:y3Xn3XE1C6dZuOj3e4G-UoRdzfi6S3FV558iz3lIsh4}")
    public static String agentSecret;

    public void setAgentId(Integer agentId) {
        WCCPConfig.agentId = agentId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public void setAgentSecret(String agentSecret) {
        this.agentSecret = agentSecret;
    }

}
