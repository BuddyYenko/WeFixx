package com.example.s215087038.wefixx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class ProviderDataObject {
    @SerializedName("name")
    private String name;
    private String provider_id;
    public ProviderDataObject(){}
    public ProviderDataObject(String provider_id, String name) {
        this.name = name;
        this.provider_id =provider_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return provider_id;
    }
    public void setID (String provider_id) {
        this.provider_id = provider_id;
    }
}