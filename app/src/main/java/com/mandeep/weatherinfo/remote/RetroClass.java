package com.mandeep.weatherinfo.remote;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mandeep.weatherinfo.model.ApiResult;
import com.mandeep.weatherinfo.model.WeatherList;
import com.mandeep.weatherinfo.model.WeatherMain;
import com.mandeep.weatherinfo.model.WeatherRequiredData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mandeep.weatherinfo.common.Common.APP_ID;
import static com.mandeep.weatherinfo.common.Common.TEMP_UNIT;

public class RetroClass {

    private static Retrofit instance;

    private static Retrofit getInstance() {
        if (instance == null) {
            // logging interceptor for logging in Http request
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);

            // client by using logging interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            Gson gson = new GsonBuilder().serializeNulls().create();

            instance = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return instance;
    }

    private static WeatherApiService getWeatherApiService() {
        return getInstance().create(WeatherApiService.class);
    }

    public MutableLiveData<WeatherRequiredData> getWeatherLiveData(String lat, String lng){
        final MutableLiveData<WeatherRequiredData> mutableLiveData = new MutableLiveData<>();
        WeatherApiService apiService = getWeatherApiService();
        apiService.getWeatherByLatLng(lat, lng, APP_ID, TEMP_UNIT).clone().enqueue(
                new Callback<ApiResult>() {
                    @Override
                    public void onResponse(@NotNull Call<ApiResult> call, @NotNull Response<ApiResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                // api response
                                ApiResult apiResult = response.body();
                                // list of data
                                List<WeatherList> weatherList = apiResult.getList();
                                // init weatherRequiredData class to store extracted data
                                WeatherRequiredData weatherRequiredData = new WeatherRequiredData();
                                //list contains many items, iterate them
                                for (WeatherList list : weatherList) {
                                    WeatherMain weatherMain = list.getMain();// get model main from list
                                    weatherRequiredData.setDate(list.getDt_txt()); // get date
                                    weatherRequiredData.setCurrentTemp(weatherMain.getTemp());// get current temp
                                    weatherRequiredData.setMaxTemp(weatherMain.getTemp_max());// get max temp
                                    weatherRequiredData.setMinTemp(weatherMain.getTemp_min());// get min temp
                                }
                                mutableLiveData.setValue(weatherRequiredData);
                            }
                        } else {
                            int errorCode = response.code();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ApiResult> call, @NotNull Throwable t) {

                    }
                }
        );
        return mutableLiveData;
    }
}
