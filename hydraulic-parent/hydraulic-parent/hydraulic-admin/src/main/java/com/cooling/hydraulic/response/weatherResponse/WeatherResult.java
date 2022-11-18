package com.cooling.hydraulic.response.weatherResponse;

import java.util.List;

public class WeatherResult {
    private WeatherLive now;
    private List<WeatherForecast> forecasts;

    public WeatherLive getNow() {
        return now;
    }

    public void setNow(WeatherLive now) {
        this.now = now;
    }

    public List<WeatherForecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<WeatherForecast> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public String toString() {
        return "WeatherResult{" +
                "now=" + now +
                ", forecasts=" + forecasts +
                '}';
    }
}
