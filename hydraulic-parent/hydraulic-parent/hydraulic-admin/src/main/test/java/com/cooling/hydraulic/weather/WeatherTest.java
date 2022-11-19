package com.cooling.hydraulic.weather;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.response.weatherResponse.BaiduResp;
import com.cooling.hydraulic.utils.HttpClientUtil;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherTest {

    @Test
    public void test1(){
        String url="https://api.map.baidu.com/weather/v1/?district_id=341802&data_type=now&ak=dQGZH8hm7bVFhgqQQsdnsjqyOLXGY928";
        try {
            String respStr = HttpClientUtil.getMethod(url);
            BaiduResp resp = JSON.parseObject(respStr, BaiduResp.class);
            System.out.println("============================================");
            System.out.println(respStr);
            System.out.println(resp.toString());
            System.out.println("============================================");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        DateTimeFormatter dfDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(LocalDateTime.now().format(dfDateTime));
        System.out.println(LocalDateTime.parse(LocalDateTime.now().format(dfDateTime), dfDateTime));
        System.out.println(LocalDateTime.now());
    }
}