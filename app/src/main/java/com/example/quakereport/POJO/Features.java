package com.example.quakereport.POJO;

import com.google.gson.annotations.SerializedName;

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
