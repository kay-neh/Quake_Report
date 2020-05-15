package com.example.quakereport.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "quake_data", indices = {@Index(value = {"eventId"},unique = true)})
public class QuakeData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String eventId;

    private double magnitude;

    private String place;

    private long time;

    public QuakeData(int id, String eventId, double magnitude, String place, long time) {
        this.id = id;
        this.eventId = eventId;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
    }

    @Ignore
    public QuakeData(String eventId, double magnitude, String place, long time) {
        this.eventId = eventId;
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
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
}
