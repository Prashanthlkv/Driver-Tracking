package com.stepintech.driverapp.drivertracking;

import android.Manifest;
import android.content.pm.PackageManager;

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
                locationPermissionGranted();
            } else {
                locationPermissionNotGranted();
            }
        }
    }

    public void requestLocationPermission() {
        if (!Utils.isLocationPermissionGranted(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }else {
            locationPermissionGranted();
        }
    }
}