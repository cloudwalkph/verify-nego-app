package com.cloudwalkdigital.verifynegotiator.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alleoindong on 6/19/17.
 */

public class Hit extends RealmObject {
    @PrimaryKey
    private Integer id;

    @SerializedName("project_id")
    private Integer projectId;

    @SerializedName("hit_timestamp")
    private String hitTimestamp;

    private String name;

    @SerializedName("school_name")
    private String schoolName;

    private String email;

    @SerializedName("contact_number")
    private String contactNumber;

    private String designation;

    private String address;

    @SerializedName("other_details")
    private String otherDetails;

    private String image;

    private String location;

    public Hit() {
    }

    public Hit(Integer id, Integer projectId, String hitTimestamp,
               String name, String schoolName, String email, String contactNumber,
               String designation, String address, String otherDetails, String image,
               String location) {
        this.id = id;
        this.projectId = projectId;
        this.hitTimestamp = hitTimestamp;
        this.name = name;
        this.schoolName = schoolName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.designation = designation;
        this.address = address;
        this.otherDetails = otherDetails;
        this.image = image;
        this.location = location;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getHitTimestamp() {
        return hitTimestamp;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public String getAddress() {
        return address;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }
}
