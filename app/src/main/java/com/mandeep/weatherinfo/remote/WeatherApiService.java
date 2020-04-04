package com.mandeep.weatherinfo.remote;

import com.mandeep.weatherinfo.model.ApiResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("forecast")
    Call<ApiResult> getWeatherByLatLng(@Query("lat") String lat,
                                       @Query("lon") String lng,
                                       @Query("appid") String appId,
                                       @Query("units") String units);
}
