package com.stepintech.driverapp.drivertracking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class SplashActivity  extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private Button mBtnNavigate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!Utils.isGooglePlayServicesAvailable(this)){
            Utils.showToast(this , getString(R.string.str_playservice_not_available));
            return;
        }

        mBtnNavigate = findViewById(R.id.btn_navigate);
        mBtnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBtnNavigate.getText().equals(getString(R.string.allow_location_permission))){
                    requestLocationPermission();
                }else if(mBtnNavigate.getText().equals(getString(R.string.str_enable_gps))){
                    Utils.openLocationSettings(SplashActivity.this);
                }else {
                    startLandingActivity();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG , "onResume");
        requestLocationPermission();
    }

    @Override
    void locationPermissionGranted() {
        Log.d(TAG , "locationPermissionGranted");
        if(Utils.isGPSEnabled(this)){
            startLandingActivity();
        }else {
            mBtnNavigate.setVisibility(View.VISIBLE);
            mBtnNavigate.setText(getString(R.string.str_enable_gps));
        }

    }

    @Override
    void locationPermissionNotGranted() {
        Log.d(TAG , "locationPermissionNotGranted");
        mBtnNavigate.setVisibility(View.VISIBLE);
        mBtnNavigate.setText(getString(R.string.allow_location_permission));

    }

    private void startLandingActivity(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
                return;
            }
        }

        Log.d(TAG , "startLandingActivity");
        if(isAllRequiredPermissionGranted()) {
            startActivity(LandingActivity.getIntent(this));
            finish();
        }
    }

}
