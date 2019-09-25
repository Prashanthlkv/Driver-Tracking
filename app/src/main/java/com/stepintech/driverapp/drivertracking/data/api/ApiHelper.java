package com.stepintech.driverapp.drivertracking.data.api;

import android.location.Location;
import android.util.Log;

import com.stepintech.driverapp.drivertracking.Utils;
import com.stepintech.driverapp.drivertracking.data.locationmanager.LocationManager;
import com.stepintech.driverapp.drivertracking.model.ApiResponse;
import com.stepintech.driverapp.drivertracking.model.LocationDetail;
import com.stepintech.driverapp.drivertracking.model.SendLocationRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stepintech.driverapp.drivertracking.Constants.MIN_DATA_NEEDED_TO_SYNC;
import static com.stepintech.driverapp.drivertracking.Constants.URL_SEND_DRIVER_LOCATION;

/**
 * Singleton class which manages all server API communication
 */
public class ApiHelper{

    private static final String TAG = "ApiHelper";

    private ApiService mService;
    private static ApiHelper sApiHelper;

    private ApiHelper(){
        mService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
    }

    /**
     * Method which returns instance of {@link ApiHelper}
     * @return ApiHelper
     */
    public static synchronized ApiHelper getInstance(){
        if(sApiHelper ==null){
            sApiHelper = new ApiHelper();
        }
        return sApiHelper;
    }


    public void sendLocationData(String driverId, String tripCode){
        Log.i(TAG , "sendLocationData");
        int dataCount = LocationManager.getInstance().getLocationDataCount();
        if(dataCount >= MIN_DATA_NEEDED_TO_SYNC){
            //Form request
            Log.i(TAG , "Sending location data to server");
            LocationManager.getInstance().setPublishingData(true);

            //Create API request
            SendLocationRequest request = new SendLocationRequest();
            request.setDriverId(driverId);
            request.setTripCode(tripCode);
            request.setTime(Utils.getTimeTimeStamp());
            request.setDate(Utils.getDateFromTimeStamp());
            List<LocationDetail> locationDetails = new ArrayList<>();
            List<Location>locations =  LocationManager.getInstance().getLocationData();
            if(locations != null){
                for (Location location:locations
                ) {
                    LocationDetail locationDetail = new LocationDetail();
                    locationDetail.setLat(String.valueOf(location.getLatitude()));
                    locationDetail.setLng(String.valueOf(location.getLongitude()));
                    locationDetail.setTime(Utils.getTimeFromTimeStamp(location.getTime()));
                    locationDetails.add(locationDetail);
                }
            }
            request.setLocationDetails(locationDetails);

            Call<ApiResponse> call = mService.sendLocationData(URL_SEND_DRIVER_LOCATION , request);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Log.i(TAG , "SUCCESS");
                    if(response.isSuccessful()){
                        LocationManager.getInstance().setPublishingData(false);
                        LocationManager.getInstance().setDataSyncStatus(true);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.i(TAG , "onFailure");
                    LocationManager.getInstance().setPublishingData(false);
                    LocationManager.getInstance().setDataSyncStatus(false);
                }
            });

        }
    }
}
