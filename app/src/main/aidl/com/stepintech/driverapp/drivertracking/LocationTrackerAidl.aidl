// LocationTrackerAidl.aidl
package com.stepintech.driverapp.drivertracking;

// Declare any non-default types here with import statements

  interface LocationTrackerAidl {
   oneway void startLocationUpdates();
   oneway void stopLocationUpdates();
}
