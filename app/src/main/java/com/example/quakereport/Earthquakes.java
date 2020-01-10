package com.example.quakereport;

public class Earthquakes {

    private double magnitude;
    private String place;
    private long time;

    public Earthquakes(double magnitude, String place, long time) {
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

}
