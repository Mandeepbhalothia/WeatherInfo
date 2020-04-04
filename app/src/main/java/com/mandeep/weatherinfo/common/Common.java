package com.mandeep.weatherinfo.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Common {
    public static final String APP_ID = "cb874bfb5b0229ecef2117d76bfa2d8e";
    public static final String TEMP_UNIT = "metric";

    public Location getCurrentLoc(Context context) {
        Location location = null;
        try {
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

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
                if (isNetworkEnabled) {

                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    final Location[] locationGps = {locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER)};

                    // request location update by gps provider
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                            , 1000, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    if (location != null)
                                        locationGps[0] = location;
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
                            });

                    if (locationGps[0] != null) {
                        location = locationGps[0];
                    }
                }
            }
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "getCurrentLoc: " + e.toString());
        }

        return location;
    }
}
