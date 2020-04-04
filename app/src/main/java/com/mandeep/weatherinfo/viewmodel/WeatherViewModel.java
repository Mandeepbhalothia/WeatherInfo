package com.mandeep.weatherinfo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mandeep.weatherinfo.model.WeatherRequiredData;
import com.mandeep.weatherinfo.remote.RetroClass;

public class WeatherViewModel extends ViewModel {
    private RetroClass retroClass = new RetroClass();

    public MutableLiveData<WeatherRequiredData> getMutableLiveData(String lat, String lng){
        return retroClass.getWeatherLiveData(lat, lng);
    }
}
