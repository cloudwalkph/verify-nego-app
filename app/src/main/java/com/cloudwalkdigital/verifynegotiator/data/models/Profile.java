package com.cloudwalkdigital.verifynegotiator.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alleoindong on 6/19/17.
 */

public class Profile {
    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("middle_name")
    private String middleName;

    private String gender;

    public Profile() {
    }

    public Profile(String firstName, String lastName, String middleName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getGender() {
        return gender;
    }
}
