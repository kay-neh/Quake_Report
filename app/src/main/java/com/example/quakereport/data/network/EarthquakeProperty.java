package com.example.quakereport.data.network;

import com.example.quakereport.data.database.Earthquake;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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

    // Method for parsing Network DTO to Database DTO
    public List<Earthquake> asDatabaseModel(){
        List<Earthquake> earthquakeList = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            earthquakeList.add(new Earthquake(features.get(i).getProperties().getEventId(),
                    features.get(i).getProperties().getMagnitude(),
                    features.get(i).getProperties().getPlace(),
                    features.get(i).getProperties().getTime(),
                    features.get(i).getProperties().getUrl(),
                    features.get(i).getGeometry().getCoordinates()[0],
                    features.get(i).getGeometry().getCoordinates()[1],
                    features.get(i).getGeometry().getCoordinates()[2]));
        }
        return earthquakeList;
    }

    // Features are contained in EarthquakeProperty
    public class Features {

        @SerializedName("properties")
        private Properties properties;

        @SerializedName("geometry")
        private Geometry geometry;

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
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

        @SerializedName("url")
        private String url;

        public Properties(String eventId, double magnitude, String place, long time, String url) {
            this.eventId = eventId;
            this.magnitude = magnitude;
            this.place = place;
            this.time = time;
            this.url = url;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class Geometry {

        @SerializedName("coordinates")
        private double[] coordinates;

        public Geometry(double[] coordinates) {
            this.coordinates = coordinates;
        }

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }



}



