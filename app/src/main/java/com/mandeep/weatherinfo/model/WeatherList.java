package com.mandeep.weatherinfo.model;

public class WeatherList {

    private String dt_txt;
    private WeatherMain main;

    public WeatherList() {
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }
}
