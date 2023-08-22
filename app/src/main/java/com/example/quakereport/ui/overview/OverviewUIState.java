package com.example.quakereport.ui.overview;

public class OverviewUIState {

    private String eventId;

    private double magnitude;

    private String place;

    private long time;

    public OverviewUIState(String eventId, double magnitude, String place, long time) {
        this.eventId = eventId;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
