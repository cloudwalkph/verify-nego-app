package com.cloudwalkdigital.verifynegotiator.data.models.events;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alleoindong on 6/19/17.
 */

public class Event extends RealmObject {
    @PrimaryKey
    protected Integer id;
    protected String name;
    protected String status;

    public Event() {
    }

    public Event(Integer id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
