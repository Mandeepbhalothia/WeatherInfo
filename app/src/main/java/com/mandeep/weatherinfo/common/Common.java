package com.mandeep.weatherinfo.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Common {
    public static final String APP_ID = "cb874bfb5b0229ecef2117d76bfa2d8e";
    public static final String TEMP_UNIT = "metric";
    private Location location = null;

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null;
    }

    public Location getCurrentLoc(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        try {

            // check locationManager
            if (locationManager == null) {
                Toast.makeText(context, "Something wrong with device. LocationManager is null",
                        Toast.LENGTH_SHORT).show();
                return null;
            }
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            Log.d("isGPSEnabled", "=" + isGPSEnabled);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.d("isNetworkEnabled", "=" + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Toast.makeText(context, "No location Provider is available",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Location Permission Missing",
                                Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                String SELECTED_PROVIDER;
                if (isGPSEnabled) {
                    SELECTED_PROVIDER = LocationManager.GPS_PROVIDER;
                    Location locationGps = locationManager.getLastKnownLocation(SELECTED_PROVIDER);

                    if (locationGps != null) {
                        location = locationGps;
                    } else {
                        // request location update by gps provider
                        locationManager.requestLocationUpdates(SELECTED_PROVIDER
                                , 1000, 0, locationListener);
                    }
                } else {
//                    Toast.makeText(context, "GPS is off, Location is provided by Network", Toast.LENGTH_SHORT).show();
                    SELECTED_PROVIDER = LocationManager.NETWORK_PROVIDER;
                    location = locationManager.getLastKnownLocation(SELECTED_PROVIDER);
                    if (location == null) {
                        // request location update by network provider
                        locationManager.requestLocationUpdates(SELECTED_PROVIDER
                                , 1000, 0, locationListener);
                    }

                }
            }
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "getCurrentLoc: " + e.toString());
        }
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
        return location;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location locationChanged) {
            location = locationChanged;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
