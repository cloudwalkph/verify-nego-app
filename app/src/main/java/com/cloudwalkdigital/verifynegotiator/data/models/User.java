package com.cloudwalkdigital.verifynegotiator.data.models;

/**
 * Created by alleoindong on 6/19/17.
 */

public class User {
    private Integer id;
    private Group group;
    private Profile profile;
    private String email;

    public User() {

    }

    public User(Integer id, Group group, Profile profile, String email) {
        this.id = id;
        this.group = group;
        this.profile = profile;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getEmail() {
        return email;
    }
}
