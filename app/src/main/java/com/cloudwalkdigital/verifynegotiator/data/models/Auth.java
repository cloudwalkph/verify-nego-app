package com.cloudwalkdigital.verifynegotiator.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alleoindong on 6/19/17.
 */

public class Auth {
    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("espires_in")
    private long expiresIn;

    public Auth() {

    }

    public Auth(String refreshToken, String accessToken, long expiresIn) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}