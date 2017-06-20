package com.cloudwalkdigital.verifynegotiator.data.models.events.remote;

import com.cloudwalkdigital.verifynegotiator.data.models.events.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by alleoindong on 6/19/17.
 */

public interface EventsService {
    @GET("api/v1/events")
    Call<List<Event>> getAssignedEvents(@Header("Authorization") String accessToken);
}
