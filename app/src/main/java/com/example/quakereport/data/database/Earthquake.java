package com.example.quakereport.data.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "earthquake", indices = {@Index(value = {"eventId"}, unique = true)})
public class Earthquake {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String eventId;

    private double magnitude;

    private String place;

    private long time;

    private String url;

    private double longitude;

    private double latitude;

    private double depth;

    public Earthquake(int id, String eventId, double magnitude, String place, long time, String url, double longitude, double latitude, double depth) {
        this.id = id;
        this.eventId = eventId;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.depth = depth;
    }

    @Ignore
    public Earthquake(String eventId, double magnitude, String place, long time, String url, double longitude, double latitude, double depth) {
        this.eventId = eventId;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.depth = depth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
