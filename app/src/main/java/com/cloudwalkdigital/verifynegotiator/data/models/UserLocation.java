package com.cloudwalkdigital.verifynegotiator.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alleoindong on 6/20/17.
 */

public class UserLocation extends RealmObject {
    @PrimaryKey
    private Integer id;

    @SerializedName("user_id")
    private Integer userId;

    private Double lat;

    private Double lng;

    public UserLocation() {
    }

    public UserLocation(Integer id, Integer userId, Double lat, Double lng) {
        this.id = id;
        this.userId = userId;
        this.lat = lat;
        this.lng = lng;
    }

    public UserLocation(Integer userId, Double lat, Double lng) {
        this.userId = userId;
        this.lat = lat;
        this.lng = lng;
    }

    public UserLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
