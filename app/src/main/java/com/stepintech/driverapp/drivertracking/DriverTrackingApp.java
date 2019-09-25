package com.stepintech.driverapp.drivertracking;

import android.app.Application;

import com.stepintech.driverapp.drivertracking.data.api.ApiHelper;
import com.stepintech.driverapp.drivertracking.data.locationmanager.LocationManager;

import static com.stepintech.driverapp.drivertracking.DriverTrackingPreference.Key.KEY_DRIVER_ID;
import static com.stepintech.driverapp.drivertracking.DriverTrackingPreference.Key.KEY_TRIP_CODE;

public class DriverTrackingApp extends Application {
    private static final String TAG = "DriverTrackingApp";
    @Override
    public void onCreate() {
        super.onCreate();
        //Keep the shared instances ready to access.
        ApiHelper.getInstance();
        LocationManager.getInstance();
        //Add static data to preference
        DriverTrackingPreference.getInstance(this).put(KEY_DRIVER_ID , "12");
        DriverTrackingPreference.getInstance(this).put(KEY_TRIP_CODE , "11");

    }
}
