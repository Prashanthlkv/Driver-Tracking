package com.stepintech.driverapp.drivertracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.stepintech.driverapp.drivertracking.Constants.PERMISSIONS_REQUEST_LOCATION;

public abstract class BaseActivity extends AppCompatActivity {

    abstract void locationPermissionGranted();
    abstract void locationPermissionNotGranted();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationPermissionGranted();
                    } else {
                        requestBackgroundLocationPermission();
                    }
                }else {
                    locationPermissionGranted();
                }
            } else {
                locationPermissionNotGranted();
            }
        }
    }

    public void requestLocationPermission() {
        if (!Utils.isBackgroundLocationPermissionGranted(this)) {
            String[]manifestPermission;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                manifestPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION ,
                        Manifest.permission.ACCESS_FINE_LOCATION};
            }else {
                manifestPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION ,
                        Manifest.permission.ACCESS_FINE_LOCATION};
            }
            ActivityCompat.requestPermissions(this,
                    manifestPermission,
                    PERMISSIONS_REQUEST_LOCATION);
        }else {
            locationPermissionGranted();
        }
    }

    private void requestBackgroundLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }
    }

    boolean isAllRequiredPermissionGranted(){
        return Utils.isBackgroundLocationPermissionGranted(this)
                && Utils.isGPSEnabled(this);
    }
}