package com.stepintech.driverapp.drivertracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static com.stepintech.driverapp.drivertracking.LocationTrackerService.KEY_SERVICE_IDENTIFIER;
import static com.stepintech.driverapp.drivertracking.LocationTrackerService.START_TRACKING;

public class LocationTrackerStatusReceiver extends BroadcastReceiver {

    private static final String TAG = "LocationTrackerReceiver";

    @Override
    public void onReceive(Context context, Intent i) {
        Log.d(TAG , "onReceive");
        Intent intent = new Intent(context, LocationTrackerService.class);
        intent.putExtra(KEY_SERVICE_IDENTIFIER , START_TRACKING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}
