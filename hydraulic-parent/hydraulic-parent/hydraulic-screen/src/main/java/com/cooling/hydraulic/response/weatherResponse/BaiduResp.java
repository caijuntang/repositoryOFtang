package com.cooling.hydraulic.response.weatherResponse;

public class BaiduResp {

    private String status;
    private String message;
    private WeatherResult result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WeatherResult getResult() {
        return result;
    }

    public void setResult(WeatherResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaiduResp{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
