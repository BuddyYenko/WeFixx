package com.example.s215087038.wefixx.models;

public class Request {
    private String request_date, request_type, status, description;

    public Request() {
    }

    public Request(String request_date, String request_type, String status, String description) {
        this.request_date = request_date;
        this.request_type = request_type;
        this.status = status;
        this.description = description;
    }

    public String getRequestDate() {
        return request_date;
    }

    public void setRequestDatte(String name) {
        this.request_date = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescrription() {
        return request_type;
    }

    public void setDescription(String request_type) {
        this.request_type = request_type;
    }

    public String getRequestType() {
        return request_type;
    }

    public void setRequestType(String request_type) {
        this.request_type = request_type;
    }
}
