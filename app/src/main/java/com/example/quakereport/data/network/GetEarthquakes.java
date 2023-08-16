package com.example.quakereport.data.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetEarthquakes {
    @GET("/fdsnws/event/1/query?format=geojson&orderby=time&limit=50")
    Call<EarthquakeProperty> getAllEarthquakes();
}
