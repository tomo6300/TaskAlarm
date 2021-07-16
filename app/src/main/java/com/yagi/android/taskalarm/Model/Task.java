package com.yagi.android.taskalarm.Model;


import android.widget.ListView;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


public class Task extends RealmObject {

    //@PrimaryKey
    public String title;
    public String safeline;
    public String passline;
    public String deadline;
    public String updateDate;

    public RealmList SmallTasks;

    public String id;
    public String achieve;

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<SmallTask> getSmallTasks() {
        return SmallTasks;
    }

    public void setSmallTasks(RealmList<SmallTask> SmallTasks) {
        this.SmallTasks = SmallTasks;
    }*/

}
