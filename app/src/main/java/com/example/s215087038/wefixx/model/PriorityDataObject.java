package com.example.s215087038.wefixx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class PriorityDataObject {
    @SerializedName("name")
    private String name;
    private String priority_id, turnaround;
    public PriorityDataObject(){}
    public PriorityDataObject(String priority_id, String name, String turnaround) {
        this.name = name;
        this.priority_id =priority_id;
        this.turnaround = turnaround;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTurnaround() {
        return turnaround;
    }
    public void setTurnaround (String turnaround) {
        this.turnaround = turnaround;
    }
    public String getID() {
        return priority_id;
    }
    public void setID (String priority_id) {
        this.priority_id = priority_id;
    }
}
