package com.example.quakereport.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Earthquakes {

    @SerializedName("features")
    private List<Features> features;

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }
}
