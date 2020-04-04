package com.mandeep.weatherinfo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mandeep.weatherinfo.model.WeatherRequiredData;
import com.mandeep.weatherinfo.remote.RetroClass;

import java.util.ArrayList;

public class WeatherViewModel extends ViewModel {
    private RetroClass retroClass = new RetroClass();
    private MutableLiveData<ArrayList<WeatherRequiredData>> mutableLiveDataList;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<ArrayList<WeatherRequiredData>> getMutableLiveData(String lat, String lng){
        if (mutableLiveDataList==null){
            isLoading.setValue(true);
            mutableLiveDataList = retroClass.getWeatherLiveData(lat,lng);
            isLoading.setValue(false);
        }
        return mutableLiveDataList;
//        return retroClass.getWeatherLiveData(lat, lng); // to fetch data every time
    }

    public MutableLiveData<Boolean> getIsLoading(){
        return isLoading;
    }
}
