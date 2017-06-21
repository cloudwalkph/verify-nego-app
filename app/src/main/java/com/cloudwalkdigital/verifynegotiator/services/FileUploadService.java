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

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alleoindong on 6/21/17.
 */

public class FileUploadService extends IntentService {
    @Inject Retrofit retrofit;
    @Inject SharedPreferences sharedPreferences;
    @Inject SessionManager sessionManager;

    private static final String TAG = "FILEUPLOADSERVICE";
    private int hitId;

    public FileUploadService() {
        super("file-upload-service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ((App) getApplication()).getNetComponent().inject(this);

        hitId = intent.getIntExtra("hitId", 0);
        String photoPath = intent.getStringExtra("photoPath");

        if (hitId == 0) {
            return;
        }

        File file = new File(photoPath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        // get the auth
        Auth auth = sessionManager.getAuthInformation();

        APIService service = retrofit.create(APIService.class);
        Call<Hit> call = service.uploadHitImage("Bearer " + auth.getAccessToken(), hitId, body);

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
