package com.cooling.hydraulic.response.weatherResponse;

public class WeatherLive {

    //天气
    private String text;
    //温度
    private String temp;
    //风力
    private String wind_class;
    //风向
    private String wind_dir;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind_class() {
        return wind_class;
    }

    public void setWind_class(String wind_class) {
        this.wind_class = wind_class;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    @Override
    public String toString() {
        return "WeatherLive{" +
                "text='" + text + '\'' +
                ", temp='" + temp + '\'' +
                ", wind_class='" + wind_class + '\'' +
                ", wind_dir='" + wind_dir + '\'' +
                '}';
    }
}
