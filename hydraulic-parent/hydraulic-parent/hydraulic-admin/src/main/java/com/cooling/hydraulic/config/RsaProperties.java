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
public class RsaProperties {

    public static String privateKey;

    @Value("${rsa.private_key}")
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


}