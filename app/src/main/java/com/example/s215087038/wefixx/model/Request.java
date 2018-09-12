package com.example.s215087038.wefixx.model;

public class Request {
    private String fault_id, request_date,fault_type_id, request_type, room, description,image_url, provider, priority, comment, date_assigned, date_closed, requester, contact_number, turnaround, email, status, provider_status;
    private float rating ;

    public Request() {
    }

    public Request(String fault_id, String request_date, String fault_type_id, String request_type, String description, String room, String image_url) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.fault_type_id = fault_type_id;
        this.request_type = request_type;
        this.description = description;
        this.room = room;
        this.image_url = image_url;
    }
    public Request(String fault_id, String request_date, String request_type, String description, String image_url) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.request_type = request_type;
        this.description = description;
        this.image_url = image_url;
    }
    public Request(String fault_id, String request_date, String request_type, String description, String room, String image_url) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.request_type = request_type;
        this.description = description;
        this.room = room;
        this.image_url = image_url;
    }
    public Request(String fault_id, String request_date, String date_assigned, String request_type, String description, String room ,String image_url, String provider, String priority) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.date_assigned = date_assigned;
        this.request_type = request_type;
        this.description = description;
        this.room = room;
        this.image_url = image_url;
        this.provider = provider;
        this.priority = priority;
    }
    public Request(String fault_id, String request_date, String date_assigned, String request_type, String description, String provider, String priority, String image_url) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.date_assigned = date_assigned;
        this.request_type = request_type;
        this.description = description;
        this.image_url = image_url;
        this.provider = provider;
        this.priority = priority;
    }
    public Request(String fault_id, String request_date, String date_assigned, String date_closed, String request_type, String description, String provider, String priority, String comment, float rating,String image_url) {
        this.fault_id = fault_id;
        this.request_date = request_date;
        this.date_assigned = date_assigned;
        this.date_closed = date_closed;
        this.request_type = request_type;
        this.description = description;
        this.image_url = image_url;
        this.provider = provider;
        this.priority = priority;
        this.comment = comment;
        this.rating = rating;
    }
    //history
    public Request( String request_date, String date_assigned, String date_closed, String request_type, String description, String room, String comment, float rating , String provider, String priority, String requester, String image_url) {
        this.request_date = request_date;
        this.date_assigned = date_assigned;
        this.date_closed = date_closed;
        this.request_type = request_type;
        this.description = description;
        this.room = room;
        this.rating = rating;
        this.provider = provider;
        this.priority = priority;
        this.comment = comment;
        this.requester = requester;
        this.image_url = image_url;

    }
//delayed
public Request( String request_date, String date_assigned, String request_type, String description, String room, String requester, String priority , String turnaround, String contact_number, String email, String provider_status, String image_url) {
    this.request_date = request_date;
    this.date_assigned = date_assigned;
    this.request_type = request_type;
    this.description = description;
    this.room = room;
    this.requester = requester;
    this.priority = priority;
    this.turnaround = turnaround;
    this.contact_number = contact_number;
    this.email = email;
    this.provider_status = provider_status;
    this.image_url = image_url;

}


    public String getRequestDate() {
        return request_date;
    }

    public void setDateAssigned(String date_assigned) {
        this.date_assigned = date_assigned;
    }
    public String getDateAssigned() {
        return date_assigned;
    }

    public void setDateClosed(String date_closed) {
        this.date_closed = date_closed;
    }
    public String getDateClosed() {
        return date_closed;
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

    public String getFaultTypeID(){return fault_type_id;}
    public  void setFaultIDType(String fault_type_id){this.fault_type_id = fault_type_id;}

    public String getFaultID(){
        return fault_id;
    }
    public  void setFaultID(String fault_id){this.fault_id = fault_id;}

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    ////
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    //
    public String getTurnaround() {
        return turnaround;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber(){
        return contact_number;
    }

    public String getProviderStatus() {
        return provider_status;
    }

    public String getRequester() {
        return requester;
    }

}
