package com.stepintech.driverapp.drivertracking;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import static com.stepintech.driverapp.drivertracking.LocationTrackerService.KEY_SERVICE_IDENTIFIER;
import static com.stepintech.driverapp.drivertracking.LocationTrackerService.START_TRACKING;

public class LocationTrackerStatusReceiver extends BroadcastReceiver {

    private static final String TAG = "LocationTrackerReceiver";

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent i) {
        Log.d(TAG , "onReceive");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock screenWakeLock = null;
        if (pm != null) {
            screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        }
        if (screenWakeLock != null) {
            screenWakeLock.acquire(10*60*1000L /*10 minutes*/);
        }

        Intent intent = new Intent(context, LocationTrackerService.class);
        intent.putExtra(KEY_SERVICE_IDENTIFIER , START_TRACKING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

        if (screenWakeLock != null){
            screenWakeLock.release();
        }
    }
}
