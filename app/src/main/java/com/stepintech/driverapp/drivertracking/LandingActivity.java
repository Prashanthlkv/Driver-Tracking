package com.stepintech.driverapp.drivertracking;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.stepintech.driverapp.drivertracking.LocationTrackerService.KEY_SERVICE_IDENTIFIER;
import static com.stepintech.driverapp.drivertracking.LocationTrackerService.START_TRACKING;
import static com.stepintech.driverapp.drivertracking.LocationTrackerService.STOP_TRACKING;

public class LandingActivity extends AppCompatActivity {

    private static final String TAG = "LandingActivity";
   // private LocationTrackerAidl mLocationTrackerService = null; // For bind service
    private Button mBtnStartTracking;
    private Button mBtnStopTracking;
    private ConstraintLayout mRootLayout;

    public static Intent getIntent(Context context){
        return new Intent(context , LandingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStartTracking = findViewById(R.id.btn_start_tracking);
        mBtnStopTracking = findViewById(R.id.btn_stop_tracking);
        mRootLayout = findViewById(R.id.layout_root);

        mBtnStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTrackingService();
            }
        });

        mBtnStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTrackingService();
            }
        });

        //For bind service
        /*bindService(new Intent(this, LocationTrackerService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
    }


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
       // Log.d(TAG , "onUserLeaveHint");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) &&
                DriverTrackingPreference.getInstance(this).getBoolean(DriverTrackingPreference.Key.IS_TRACKING_ON ,
                        false)) {
            enterPictureInPictureMode();
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        //Log.d(TAG , "onUserLeaveHint"+isInPictureInPictureMode);
        if (isInPictureInPictureMode) {
            if(getSupportActionBar() != null){
                getSupportActionBar().hide();
            }
            mBtnStartTracking.setVisibility(View.INVISIBLE);
            mBtnStopTracking.setVisibility(View.INVISIBLE);
            mRootLayout.setBackgroundResource(R.color.colorPrimary);
        } else {
            if(getSupportActionBar() != null){
                getSupportActionBar().show();
            }
            mBtnStartTracking.setVisibility(View.VISIBLE);
            mBtnStopTracking.setVisibility(View.VISIBLE);
            mRootLayout.setBackgroundResource(R.color.colorWhite);
        }
    }

    //Used for bind service
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //mLocationTrackerService = LocationTrackerAidl.Stub.asInterface(service);
            //mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //mBound = false;
        }
    };


    /**
     * Method to start the location tracking service.
     */
    private void startTrackingService(){
        Intent intent = new Intent(getApplicationContext(), LocationTrackerService.class);
        intent.putExtra(KEY_SERVICE_IDENTIFIER , START_TRACKING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else {
            startService(intent);
        }
    }


    /**
     * Method to stop the location tracking service.
     */
    private void stopTrackingService(){
        Intent intent = new Intent(getApplicationContext(), LocationTrackerService.class);
        intent.putExtra(KEY_SERVICE_IDENTIFIER , STOP_TRACKING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else {
            startService(intent);
        }
    }

}
