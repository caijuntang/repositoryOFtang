package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.enums.WeatherTypeEnum;
import com.cooling.hydraulic.response.weatherResponse.WeatherForecast;
import com.cooling.hydraulic.service.StationService;
import com.cooling.hydraulic.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/weather")
public class WeatherController {

    @Resource
    private WeatherService weatherService;

    @RequestMapping("/getWeatherNow")
    @ResponseBody
    public Object getWeatherNow(Integer stationId) {
        return weatherService.getWeatherPre(stationId);
    }


    @RequestMapping("/getWeatherWeek")
    @ResponseBody
    public Object getWaterLine(Integer stationId) {
        List<WeatherForecast> forecastList = weatherService.getWeatherAllByStation(stationId);
        if(null==forecastList||forecastList.isEmpty()){
            return null;
        }
        List<List<String>> resultList = new ArrayList<>();
        for(WeatherForecast f:forecastList){
            List<String> l = new ArrayList<>();
            l.add(f.getDate());
            l.add(f.getWeek());
            l.add(f.getText_day());
            l.add(f.getText_night());
            resultList.add(l);
        }
        return resultList;
    }

}
