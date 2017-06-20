package com.cloudwalkdigital.verifynegotiator.data;

import com.cloudwalkdigital.verifynegotiator.data.models.UserLocation;
import com.cloudwalkdigital.verifynegotiator.data.models.events.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by alleoindong on 6/19/17.
 */

public interface APIService {
    @GET("api/v1/events")
    Call<List<Event>> getAssignedEvents(@Header("Authorization") String accessToken);

    @POST("api/v1/gps")
    Call<UserLocation> updateLocation(@Header("Authorization") String accessToken, @Body UserLocation userLocation);
}
