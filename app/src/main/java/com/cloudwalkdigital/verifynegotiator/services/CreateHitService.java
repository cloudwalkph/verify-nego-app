package com.cloudwalkdigital.verifynegotiator.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cloudwalkdigital.verifynegotiator.App;
import com.cloudwalkdigital.verifynegotiator.data.APIService;
import com.cloudwalkdigital.verifynegotiator.data.models.Auth;
import com.cloudwalkdigital.verifynegotiator.data.models.Hit;
import com.cloudwalkdigital.verifynegotiator.utils.SessionManager;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alleoindong on 6/20/17.
 */

public class CreateHitService extends IntentService {
    @Inject Retrofit retrofit;
    @Inject SharedPreferences sharedPreferences;
    @Inject SessionManager sessionManager;

    private static final String TAG = "CREATEHITSERVICE";
    private int projectId;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public CreateHitService() {
        super("create-hit-service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ((App) getApplication()).getNetComponent().inject(this);

        projectId = intent.getIntExtra("projectId", 0);
        if (projectId == 0) {
            return;
        }

        // get the auth
        Auth auth = sessionManager.getAuthInformation();
        Gson gson = new Gson();
        String json = intent.getStringExtra("hit");
        Hit hit = gson.fromJson(json, Hit.class);

        APIService service = retrofit.create(APIService.class);
        Call<Hit> call = service.createHit("Bearer " + auth.getAccessToken(), projectId, hit);
//        call.enqueue(new Callback<Hit>() {
//            @Override
//            public void onResponse(Call<Hit> call, Response<Hit> response) {
//                Log.i(TAG, "onAPIResponse: " + response.raw().toString());
//            }
//
//            @Override
//            public void onFailure(Call<Hit> call, Throwable t) {
//                Log.i(TAG, "onAPIResponseFailed: " + t.getMessage());
//            }
//        });

        try {
            Response<Hit> response = call.execute();
            Log.i(TAG, "onAPIResponse: " + response.raw().toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onAPIResponseFailed: " +  e.getMessage());
        }

        return;
    }
}
