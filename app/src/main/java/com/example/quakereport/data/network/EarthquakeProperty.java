package com.example.quakereport.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EarthquakeProperty {

    @SerializedName("features")
    private List<Features> features;

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }



    // Features are contained in EarthquakeProperty
    public class Features {

        @SerializedName("type")
        private String type;

        @SerializedName("properties")
        private Properties properties;

        public String getType() {
            return type;
        }

        public void setType(String type){
            this.type = type;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }


    // Properties are contained in Features
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


}



