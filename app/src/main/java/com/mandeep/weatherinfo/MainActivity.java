package com.mandeep.weatherinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mandeep.weatherinfo.common.Common;
import com.mandeep.weatherinfo.model.ApiResult;
import com.mandeep.weatherinfo.model.WeatherList;
import com.mandeep.weatherinfo.model.WeatherMain;
import com.mandeep.weatherinfo.model.WeatherRequiredData;
import com.mandeep.weatherinfo.remote.RetroClass;
import com.mandeep.weatherinfo.remote.WeatherApiService;
import com.mandeep.weatherinfo.viewmodel.WeatherViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mandeep.weatherinfo.common.Common.APP_ID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get current loc without check
        Location currentLoc = new Common().getCurrentLoc(this);

        if (currentLoc != null) {
            WeatherViewModel weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
            weatherViewModel.getMutableLiveData("" + currentLoc.getLatitude(),
                    "" + currentLoc.getLongitude()).observe(this, new Observer<WeatherRequiredData>() {
                @Override
                public void onChanged(WeatherRequiredData weatherRequiredData) {
                    if (weatherRequiredData != null) {
                        Log.d(TAG, "onCreate: weatherRequiredData:- " + weatherRequiredData.getDate()
                                + " currentTemp " + weatherRequiredData.getCurrentTemp() + " maxTemp "
                                + weatherRequiredData.getMaxTemp()
                                + " minTemp " + weatherRequiredData.getMinTemp());
                    }
                }
            });
        }
    }
}
