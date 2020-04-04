package com.mandeep.weatherinfo;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mandeep.weatherinfo.common.Common;
import com.mandeep.weatherinfo.model.WeatherRequiredData;
import com.mandeep.weatherinfo.viewmodel.WeatherViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CardView bottomCv;
    private TextView currentTempTv, day2Tv, day2TempTv, day3Tv, day3TempTv,
            day4Tv, day4TempTv, day5Tv, day5TempTv, day6Tv, day6TempTv;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        // get current loc 
        Location currentLoc = getCurrentLoc();

        if (currentLoc != null) {
            getWeatherData(currentLoc);
        }
    }

    private void initUi() {
        progressbar = findViewById(R.id.progressBar);
        bottomCv = findViewById(R.id.bottomCV);
        currentTempTv = findViewById(R.id.currentTempTv);
        day2Tv = findViewById(R.id.day2Tv);
        day3Tv = findViewById(R.id.day3Tv);
        day4Tv = findViewById(R.id.day4Tv);
        day5Tv = findViewById(R.id.day5Tv);
        day6Tv = findViewById(R.id.day6Tv);
        day2TempTv = findViewById(R.id.day2TempTv);
        day3TempTv = findViewById(R.id.day3TempTv);
        day4TempTv = findViewById(R.id.day4TempTv);
        day5TempTv = findViewById(R.id.day5TempTv);
        day6TempTv = findViewById(R.id.day6TempTv);
        // set background to bottomCv
        bottomCv.setBackgroundResource(R.drawable.card_view_bg);
    }

    private Location getCurrentLoc() {
        return new Common().getCurrentLoc(this);
    }

    private void getWeatherData(Location currentLoc) {
        WeatherViewModel weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        // show progressBar to user
        showProgress();
        weatherViewModel.getMutableLiveData("" + currentLoc.getLatitude(),
                "" + currentLoc.getLongitude()).observe(this, new Observer<ArrayList<WeatherRequiredData>>() {
            @Override
            public void onChanged(ArrayList<WeatherRequiredData> weatherRequiredDataList) {
                hideProgress();// hide progressBar
                if (weatherRequiredDataList != null) {
                    int pos = 1;
                    for (WeatherRequiredData weatherRequiredData : weatherRequiredDataList) {
                        setData(weatherRequiredData, pos);
                        pos++;
                    }
                    // show bottom cardView with animation
                    startBottomAnim();
                }
            }
        });
    }

    private void startBottomAnim() {
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_top);
        bottomCv.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bottomCv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setData(WeatherRequiredData weatherRequiredData, int pos) {
        String minTemp = weatherRequiredData.getMinTemp();
        String maxTemp = weatherRequiredData.getMaxTemp();
        String mergedTemp = minTemp + "\n/\n" + maxTemp;
        switch (pos) {
            case 1:
                String currentTemp = weatherRequiredData.getCurrentTemp()+getResources().getString(R.string.degree);
                currentTempTv.setText(currentTemp);
                break;
            case 2:
                day2Tv.setText(getDay(1));
                day2TempTv.setText(mergedTemp);
                break;
            case 3:
                day3Tv.setText(getDay(2));
                day3TempTv.setText(mergedTemp);
                break;
            case 4:
                day4Tv.setText(getDay(3));
                day4TempTv.setText(mergedTemp);
                break;
            case 5:
                day5Tv.setText(getDay(4));
                day5TempTv.setText(mergedTemp);
                break;
            case 6:
                day6Tv.setText(getDay(5));
                day6TempTv.setText(mergedTemp);
                break;
        }
    }

    private String getDay(int addInCurrent) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
        calendar.add(Calendar.DATE, addInCurrent);
        return sdf.format(calendar.getTime());
    }

    private void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressbar.setVisibility(View.GONE);
    }
}
