package com.stepintech.driverapp.drivertracking;

public final class Constants {

    //Service constants
    static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5 * 1000; // in ms
    static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final long MAX_WAIT_TIME = UPDATE_INTERVAL_IN_MILLISECONDS * 3;
    //AlarmHandler constants
    static final String LOCATION_TRACKER_RECEIVER_FILTER = "com.stepintech.driverapp.drivertracking.receiver";
    static final int ALARM_INTERVAL = 1*60*1000; //5 mins
    //Permission constants
    static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    //Data sync constants
     public static final int MIN_DATA_NEEDED_TO_SYNC = 10;
     //API constants
    public static final String URL_SEND_DRIVER_LOCATION = "http://ourwork.in/LoadEazyApp/Driver/driver_tracking1.php";
}
