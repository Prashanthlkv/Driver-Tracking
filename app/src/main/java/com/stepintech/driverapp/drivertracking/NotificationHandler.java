package com.stepintech.driverapp.drivertracking;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import static com.stepintech.driverapp.drivertracking.LocationTrackerService.KEY_IDENTIFIER_NOTIFICATION;

class NotificationHandler {

    private Context mContext;
    private static final String CHANNEL_ID = "stepin_channel";
    private static final int NOTIFICATION_ID = 12345678;

    NotificationHandler(Context context){
        mContext = context;
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID,
                            name, NotificationManager.IMPORTANCE_DEFAULT);
            // Set the Notification Channel for the Notification Manager.
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }
    }

    Notification getLocationTrackerServiceNotification() {
        Intent intent = new Intent(mContext, LocationTrackerService.class);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(KEY_IDENTIFIER_NOTIFICATION, true);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext , LandingActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext.getApplicationContext()
                , CHANNEL_ID)
                .addAction(R.drawable.ic_stop_white_24dp, mContext.getString(R.string.stop_location_updates),
                        activityPendingIntent)
                .setContentText(mContext.getString(R.string.str_tracking_location))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(mContext.getString(R.string.str_tracking_location))
                .setWhen(System.currentTimeMillis());
        return builder.build();
    }



}
