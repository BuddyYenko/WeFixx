package com.example.s215087038.wefixx.models;

public class Request {
    private String fault_id, request_date, request_type, room, description,image_url;

    public Request() {
    }

    public Request(String fault_id, String request_date, String request_type, String description, String room, String image_url) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.request_type = request_type;
        this.description = description;
        this.room = room;
        this.image_url = image_url;
    }


    public String getRequestDate() {
        return request_date;
    }

    public void setRequestDate(String request_date) {
        this.request_date = request_date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestType() {
        return request_type;
    }

    public void setRequestType(String request_type) {
        this.request_type = request_type;
    }

    public String getFaultID(){
        return fault_id;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }
}
