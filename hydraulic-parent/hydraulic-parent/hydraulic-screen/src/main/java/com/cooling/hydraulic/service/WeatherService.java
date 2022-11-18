package com.cooling.hydraulic.service;


import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.response.weatherResponse.BaiduResp;
import com.cooling.hydraulic.response.weatherResponse.WeatherLive;
import com.cooling.hydraulic.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final static Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final List<WeatherLive>  futureWeather = new ArrayList<>();

    //data_type数据类型有：now/fc/index/alert/fc_hour/all
    private static final String WEATHER_URL="https://api.map.baidu.com/weather/v1/?district_id={city}&data_type={type}&ak={ak}";

    public BaiduResp getWeather(String cityCode,String type) {
        String resp=null;
        try {
            String url = WEATHER_URL.replace("{city}",  cityCode).replace("{type}", type).replace("{ak}", BaseConfig.baiduKey);
            resp = HttpClientUtil.getMethod(url);
        } catch (Exception e) {
            log.error("获取天气出错",e);
        }
        if(null==resp){
            log.error("=========获取的天气为空=======");
            return null;
        }
        BaiduResp weather = JSON.parseObject(resp, BaiduResp.class);
        return weather;
    }

}
