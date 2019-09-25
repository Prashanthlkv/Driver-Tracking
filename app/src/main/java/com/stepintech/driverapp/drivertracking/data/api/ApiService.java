package com.stepintech.driverapp.drivertracking.data.api;

import com.stepintech.driverapp.drivertracking.model.ApiResponse;
import com.stepintech.driverapp.drivertracking.model.SendLocationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    @Headers({
            "Content-Type: application/json",
            "Accept-Charset: utf-8"
    })
    @POST
    Call<ApiResponse> sendLocationData(@Url String url , @Body SendLocationRequest request);
}
