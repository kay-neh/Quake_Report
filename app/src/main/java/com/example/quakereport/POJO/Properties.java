package com.example.quakereport.POJO;

import com.google.gson.annotations.SerializedName;

public class Properties {
    @SerializedName("mag")
    private double magnitude;

    @SerializedName("place")
    private String place;

    @SerializedName("time")
    private long time;

    public Properties(double magnitude, String place, long time) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
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