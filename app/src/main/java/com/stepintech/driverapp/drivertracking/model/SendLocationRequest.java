package com.stepintech.driverapp.drivertracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendLocationRequest {

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("tripCode")
    @Expose
    private String tripCode;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("location_details")
    @Expose
    private List<LocationDetail> locationDetails = null;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LocationDetail> getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(List<LocationDetail> locationDetails) {
        this.locationDetails = locationDetails;
    }


}
