package com.mandeep.weatherinfo.remote;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mandeep.weatherinfo.model.ApiResult;
import com.mandeep.weatherinfo.model.WeatherList;
import com.mandeep.weatherinfo.model.WeatherMain;
import com.mandeep.weatherinfo.model.WeatherRequiredData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    public MutableLiveData<ArrayList<WeatherRequiredData>> getWeatherLiveData(String lat, String lng) {
        final MutableLiveData<ArrayList<WeatherRequiredData>> mutableLiveData = new MutableLiveData<>();
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
                                // init arrayList to store all days data
                                ArrayList<WeatherRequiredData> weatherDataList = new ArrayList<>();
                                // arrayList to check weather data of particular date is stored or not
                                ArrayList<String> dateList = new ArrayList<>();
                                WeatherRequiredData weatherRequiredData;
                                //list contains many items, iterate them
                                for (WeatherList list : weatherList) {
                                    // init weatherRequiredData class to store extracted data
                                    weatherRequiredData = new WeatherRequiredData();

                                    WeatherMain weatherMain = list.getMain();// get model main from list
                                    String date = list.getDt_txt().split("\\s")[0];// get date only
                                    weatherRequiredData.setDate(date);
                                    weatherRequiredData.setCurrentTemp(weatherMain.getTemp());// get current temp
                                    weatherRequiredData.setMaxTemp(weatherMain.getTemp_max());// get max temp
                                    weatherRequiredData.setMinTemp(weatherMain.getTemp_min());// get min temp
                                    if (!dateList.contains(date)) { // save data if date is changed
                                        weatherDataList.add(weatherRequiredData);
                                        dateList.add(date);
                                    }
                                }
                                dateList.clear();
                                mutableLiveData.setValue(weatherDataList);
                            }
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
