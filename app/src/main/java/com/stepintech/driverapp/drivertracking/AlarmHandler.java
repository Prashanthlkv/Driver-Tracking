package com.stepintech.driverapp.drivertracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import static com.stepintech.driverapp.drivertracking.Constants.ALARM_INTERVAL;
import static com.stepintech.driverapp.drivertracking.Constants.LOCATION_TRACKER_RECEIVER_FILTER;

/**
 * Singleton class which manages application's Alarm
 */

class AlarmHandler {

    private Context mContext;
    private PendingIntent mPendingIntent;
    private static  AlarmHandler mAlarmHandler;

    private AlarmHandler(){
        //Empty constructor
    }

    private AlarmHandler(Context context){
        mContext = context.getApplicationContext();
    }


    static synchronized AlarmHandler getInstance(Context context){
        if(mAlarmHandler == null){
            mAlarmHandler = new AlarmHandler(context);
        }
        return mAlarmHandler;
    }


    /**
     * Method which register and set repeating alarm.
     */

    void setRepeatingAlarm(){
        cancelRepeatingAlarm();
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext , LocationTrackerStatusReceiver.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LOCATION_TRACKER_RECEIVER_FILTER);
        mPendingIntent = PendingIntent.getBroadcast(mContext,
                0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + ALARM_INTERVAL,
                    ALARM_INTERVAL, mPendingIntent);
        }

    }

    /**
     * Method which cancels the registered alarm.
     */
    private void cancelRepeatingAlarm(){
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null && mPendingIntent != null){
            alarmManager.cancel(mPendingIntent);
        }
    }


}
