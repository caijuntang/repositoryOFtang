package com.cooling.hydraulic.service;


import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.enums.WeatherTypeEnum;
import com.cooling.hydraulic.model.PumpDataForm;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.response.weatherResponse.BaiduResp;
import com.cooling.hydraulic.response.weatherResponse.WeatherForecast;
import com.cooling.hydraulic.response.weatherResponse.WeatherLive;
import com.cooling.hydraulic.response.weatherResponse.WeatherResult;
import com.cooling.hydraulic.utils.DateTimeUtil;
import com.cooling.hydraulic.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {
    private final static Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final Map<Integer,List<WeatherForecast>> futureWeatherMap = new ConcurrentHashMap<>();

    @Resource
    private StationRepository stationRepository;

    //data_type数据类型有：now/fc/index/alert/fc_hour/all
    private static final String WEATHER_URL="https://api.map.baidu.com/weather/v1/?district_id={city}&data_type={type}&ak={ak}";

    @PostConstruct
    void init() {
        reportWaterDetail();
    }

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

    public String getWeatherPre(Integer stationId) {
        Station station = stationRepository.getOne(stationId);
        BaiduResp weather = this.getWeather(station.getCityCode(), WeatherTypeEnum.NOW.getValue());
        WeatherResult result = weather.getResult();
        if(null==result){
            return "暂无天气数据";
        }
        return result.getNow().getText();
    }



    public List<WeatherForecast> getWeatherAllByStation(Integer stationId) {
        List<WeatherForecast> forecastList = futureWeatherMap.get(stationId);
        return forecastList;
    }

    @Scheduled(cron = "0 0 3 * * ? ")
    public void reportWaterDetail() {
        log.info("===============天气预报查询启动==================");
        List<Station> stationList = stationRepository.findAll();
        if (null == stationList||stationList.isEmpty()) {
            return;
        }
        for (Station s : stationList) {
            String cityCode = s.getCityCode();
            BaiduResp weather = this.getWeather(cityCode, WeatherTypeEnum.All.getValue());
            WeatherResult result = weather.getResult();
            if(null==result){
                continue;
            }
            List<WeatherForecast> forecasts = result.getForecasts();
            if(null==forecasts||forecasts.isEmpty()){
                continue;
            }
            futureWeatherMap.put(s.getId(),forecasts);
        }
    }



}
