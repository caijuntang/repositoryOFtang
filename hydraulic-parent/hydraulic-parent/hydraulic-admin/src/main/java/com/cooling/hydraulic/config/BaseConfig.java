package com.cooling.hydraulic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "base")
public class BaseConfig {
    private final static Logger log = LoggerFactory.getLogger(BaseConfig.class);

    @Value("${station_name_key:敬亭圩}")
    public static String stationNameKey;

    @Value("${baidu_key:dQGZH8hm7bVFhgqQQsdnsjqyOLXGY928}")
    public static String baiduKey;

    @Value("${ys_key:4de2880378ca45428cca9d6454306a3d}")
    public static String ysKey;

    @Value("${ys_secret:33c42cfc7b7f120dadef4b04273eaea7}")
    public static String ysSecret;


    public String getStationNameKey() {
        return stationNameKey;
    }

    public void setStationNameKey(String stationNameKey) {
        BaseConfig.stationNameKey = stationNameKey;
    }

    public static final List<String> stationNameList=new ArrayList<>();

    public String getBaiduKey() {
        return baiduKey;
    }

    public void setBaiduKey(String baiduKey) {
        BaseConfig.baiduKey = baiduKey;
    }

    public  String getYsKey() {
        return ysKey;
    }

    public  void setYsKey(String ysKey) {
        BaseConfig.ysKey = ysKey;
    }

    public  String getYsSecret() {
        return ysSecret;
    }

    public  void setYsSecret(String ysSecret) {
        BaseConfig.ysSecret = ysSecret;
    }

    @PostConstruct()
    private void init(){
        String[] nameArr = BaseConfig.stationNameKey.split(",");
        List list = CollectionUtils.arrayToList(nameArr);
        stationNameList.addAll(list);
        log.info("泵站名称关键词加载成功："+stationNameList.toString());
    }
}
