package com.cooling.hydraulic.config;

import com.cooling.hydraulic.service.WaterLineService;
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

    public String getStationNameKey() {
        return stationNameKey;
    }

    public void setStationNameKey(String stationNameKey) {
        BaseConfig.stationNameKey = stationNameKey;
    }

    public static final List<String> stationNameList=new ArrayList<>();


    @PostConstruct()
    private void init(){
        String[] nameArr = BaseConfig.stationNameKey.split(",");
        List list = CollectionUtils.arrayToList(nameArr);
        stationNameList.addAll(list);
        log.info("泵站名称关键词加载成功："+stationNameList.toString());
    }
}
