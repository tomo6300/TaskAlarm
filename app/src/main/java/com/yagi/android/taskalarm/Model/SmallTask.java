package com.yagi.android.taskalarm.Model;

import java.util.List;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SmallTask extends RealmObject {

    //@PrimaryKey
    public String title;
    public String safeline;
    public String passline;
    public String deadline;
    public String updateDate;
    public String achieve;

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}