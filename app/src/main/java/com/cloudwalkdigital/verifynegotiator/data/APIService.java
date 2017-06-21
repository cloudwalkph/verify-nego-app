package com.cloudwalkdigital.verifynegotiator.data;

import com.cloudwalkdigital.verifynegotiator.data.models.Hit;
import com.cloudwalkdigital.verifynegotiator.data.models.UserLocation;
import com.cloudwalkdigital.verifynegotiator.data.models.events.Event;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by alleoindong on 6/19/17.
 */

public interface APIService {
    @GET("api/v1/events")
    Call<List<Event>> getAssignedEvents(@Header("Authorization") String accessToken);

    @POST("api/v1/gps")
    Call<UserLocation> updateLocation(@Header("Authorization") String accessToken, @Body UserLocation userLocation);

    @POST("api/v1/hits/{projectId}")
    Call<Hit> createHit(@Header("Authorization") String accessToken,
                                 @Path("projectId") Integer projectId,
                                 @Body Hit hit);

    @Multipart
    @POST("api/v1/hits/images/{hitId}")
    Call<Hit> uploadHitImage(@Header("Authorization") String accessToken,
                             @Path("hitId") Integer hitId,
                             @Part MultipartBody.Part image);
}
