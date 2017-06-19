package com.cloudwalkdigital.verifynegotiator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cloudwalkdigital.verifynegotiator.LoginActivity;
import com.cloudwalkdigital.verifynegotiator.data.models.Auth;
import com.cloudwalkdigital.verifynegotiator.data.models.User;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by alleoindong on 6/19/17.
 */

public class SessionManager {
    protected SharedPreferences sharedPreferences;

    @Inject
    public SessionManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public User getUserInformation() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);

        return user;
    }

    public Auth getAuthInformation() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("auth", "");
        Auth auth = gson.fromJson(json, Auth.class);

        return auth;
    }

    public void logout(Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public Boolean isLoggedIn() {
        User user = getUserInformation();

        return user != null;
    }
}
