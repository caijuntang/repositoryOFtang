package com.cooling.hydraulic.response.weatherResponse;

public class WeatherForecast {

    private String date;
    private String week;
    //温度
    private String high;
    private String low;
    //风力
    private String wc_day;
    private String wc_night;
    //风向
    private String wd_day;
    private String wd_night;
    //天气
    private String text_day;
    private String text_night;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getWc_day() {
        return wc_day;
    }

    public void setWc_day(String wc_day) {
        this.wc_day = wc_day;
    }

    public String getWc_night() {
        return wc_night;
    }

    public void setWc_night(String wc_night) {
        this.wc_night = wc_night;
    }

    public String getWd_day() {
        return wd_day;
    }

    public void setWd_day(String wd_day) {
        this.wd_day = wd_day;
    }

    public String getWd_night() {
        return wd_night;
    }

    public void setWd_night(String wd_night) {
        this.wd_night = wd_night;
    }

    public String getText_day() {
        return text_day;
    }

    public void setText_day(String text_day) {
        this.text_day = text_day;
    }

    public String getText_night() {
        return text_night;
    }

    public void setText_night(String text_night) {
        this.text_night = text_night;
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", wc_day='" + wc_day + '\'' +
                ", wc_night='" + wc_night + '\'' +
                ", wd_day='" + wd_day + '\'' +
                ", wd_night='" + wd_night + '\'' +
                ", text_day='" + text_day + '\'' +
                ", text_night='" + text_night + '\'' +
                '}';
    }
}
