package com.example.s215087038.wefixx.models;

public class Request {
    private String request_date, request_type, room, description, id;

    public Request() {
    }

    public Request(String request_date, String request_type, String description, String room) {
        this.request_date = request_date;
        this.request_type = request_type;
        this.description = description;
        this.room = room;
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

    public String getID(){
        return id;
    }
}
