package com.example.s215087038.wefixx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class DataObject {
    @SerializedName("name")
    private String name;
    private String fault_type_id;
    public DataObject(){}
    public DataObject(String fault_type_id, String fault_type_name) {
        this.name = fault_type_name;
        this.fault_type_id =fault_type_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String fault_type_name) {
        this.name = fault_type_name;
    }

    public String getID() {
        return fault_type_id;
    }
    public void setID (String fault_type_id) {
        this.fault_type_id = fault_type_id;
    }
}
