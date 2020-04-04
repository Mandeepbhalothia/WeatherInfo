package com.mandeep.weatherinfo.model;

import java.util.List;

public class ApiResult {

    private List<WeatherList> list;

    public ApiResult() {
    }

    public List<WeatherList> getList() {
        return list;
    }

    public void setList(List<WeatherList> list) {
        this.list = list;
    }
}
