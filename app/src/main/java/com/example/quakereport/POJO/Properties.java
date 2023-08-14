package com.example.quakereport.POJO;

import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("ids")
    private String eventId;

    @SerializedName("mag")
    private double magnitude;

    @SerializedName("place")
    private String place;

    @SerializedName("time")
    private long time;

    public Properties(String eventId, double magnitude, String place, long time) {
        this.eventId = eventId;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
    }

    public String getEventId() {
        return eventId;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTime(long time) {
        this.time = time;
    }
}