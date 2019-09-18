package com.stepintech.driverapp.drivertracking;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.stepintech.driverapp.drivertracking.Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.stepintech.driverapp.drivertracking.Constants.LOCATION_TRACKER_RECEIVER_FILTER;
import static com.stepintech.driverapp.drivertracking.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;

public class LocationTrackerService extends Service {

    private static final String TAG = "LocationTrackerService";

    public static final String KEY_IDENTIFIER_NOTIFICATION =  "key_identifier_notification";
    public static final String KEY_SERVICE_IDENTIFIER =  "key_service_identifier";
    public static final String START_TRACKING = "start_tracking";
    public static final String STOP_TRACKING = "stop_tracking";

    private NotificationHandler mNotificationHandler;
    private LocationCallback mLocationCallback;
    protected LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int NOTIFICATION_ID = 12345678;
    private Handler mServiceHandler;
    private Location mLocation;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "in onCreate()");
        mNotificationHandler = new NotificationHandler(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };
        createLocationRequest();
        getLastLocation();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "in onStartCommand()");
        boolean startedFromNotification = intent.getBooleanExtra(KEY_IDENTIFIER_NOTIFICATION,
                false);
        String action = intent.getStringExtra(KEY_SERVICE_IDENTIFIER);
        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        Log.d(TAG, "Action="+action);
        if(action != null){
            switch (action){
                case START_TRACKING:
                    requestLocationUpdates();
                    startForeground(NOTIFICATION_ID,
                            mNotificationHandler.getLocationTrackerServiceNotification());
                    break;

                case STOP_TRACKING:
                    removeLocationUpdates();
                    break;
                default:break;
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "in onBind()");
        stopForeground(true);
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "in onRebind()");
        stopForeground(true);
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        startForeground(NOTIFICATION_ID,
                mNotificationHandler.getLocationTrackerServiceNotification());
        return true;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mServiceHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        notifyServiceDestroyEvent();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved");
        super.onTaskRemoved(rootIntent);
        notifyServiceDestroyEvent();
    }

    private void notifyServiceDestroyEvent(){
        if(mRequestingLocationUpdates){
            Intent intent = new Intent(this , LocationTrackerStatusReceiver.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setAction(LOCATION_TRACKER_RECEIVER_FILTER);
            sendBroadcast(intent);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void requestLocationUpdates() {
        Log.d(TAG, "Requesting location updates");
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            //Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
        AlarmHandler.getInstance(this).setRepeatingAlarm();
        DriverTrackingPreference.getInstance(this).put(DriverTrackingPreference.Key.IS_TRACKING_ON ,
                true);
        mRequestingLocationUpdates = true;
    }


    public void removeLocationUpdates() {
        Log.d(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            stopSelf();
        } catch (SecurityException unlikely) {
            //Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
        DriverTrackingPreference.getInstance(this).put(DriverTrackingPreference.Key.IS_TRACKING_ON ,
                false);
        mRequestingLocationUpdates = false;
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }


    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);
        mLocation = location;
        //Push data to server


    }
    private LocationTrackerAidl.Stub mBinder = new  LocationTrackerAidl.Stub(){

        @Override
        public void startLocationUpdates() {
            requestLocationUpdates();
        }

        @Override
        public void stopLocationUpdates() {
            removeLocationUpdates();
        }
    };

}
