package com.example.s215087038.wefixx.student;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class DataObject {
    @SerializedName("name")
    private String name;
    private String fault_type_id;
    public DataObject(){}
    public DataObject(String fault_type_id, String name) {
        this.name =name;
        this.fault_type_id =fault_type_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name, String fault_type_id) {
        this.name = name;
        this.fault_type_id = fault_type_id;
    }

    public String getID() {
        return fault_type_id;
    }
    public void setID (String fault_type_id) {
        this.fault_type_id = fault_type_id;
    }
}
//
//public class DataObject {
//    @SerializedName("name")
//    private String name, id;
//    public DataObject(){}
//    public DataObject(String name, String id) {
//        this.name = name;
//        this.id = id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getID() {
//        return id;
//    }
//    public void setID(String name) {
//        this.id = id;
//    }
//}