package com.stepintech.driverapp.drivertracking.data.locationmanager;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton class which manages {@link Location} details manipulation
 */
public class LocationManager {

    private static LocationManager sLocationManager;
    private List<Location> mLocationBuffer = Collections.synchronizedList(new ArrayList<Location>());
    private List<Location> mLocationTempBuffer = Collections.synchronizedList(new ArrayList<Location>());
    private boolean mPublishingData;


    private LocationManager(){
        //Empty constructor
    }

    /**
     * Method which returns th instance of {@link LocationManager} class.
     * @return LocationManager
     */
    public static synchronized LocationManager getInstance(){
        if(sLocationManager == null){
            sLocationManager = new LocationManager();
        }
        return sLocationManager;
    }

    /**
     * Method which adds location data to buffer
     * @param location
     */
    public synchronized void pushLocation(Location location){
        if(!mPublishingData){
            mLocationBuffer.add(location);
        }else {
            mLocationTempBuffer.add(location);
        }
    }

    /**
     * Method which sets the flag to indicate that data is publishing to server
     * @param publishingData
     */
    public synchronized void setPublishingData(boolean publishingData) {
        this.mPublishingData = publishingData;
    }

    /**
     * Method which sets the flag that data is synced to server or not
     * @param syncStatus
     */
    public void setDataSyncStatus(boolean syncStatus){
        if(syncStatus){
            mLocationBuffer.clear();
        }
        mLocationBuffer.addAll(mLocationTempBuffer);
        mLocationTempBuffer.clear();
    }


    /**
     * Method which returns the collected locations
     * @return
     */
    public synchronized List<Location> getLocationData(){
        return new ArrayList<>(mLocationBuffer);
    }


    /**
     * Method which returns the count of current collected location
     * @return
     */
    public synchronized int getLocationDataCount(){
        return mLocationBuffer.size();
    }

    /**
     * Method which resets the buffer
     */
    public synchronized void resetBuffer(){
        mLocationBuffer.clear();
        mLocationTempBuffer.clear();

    }

}
