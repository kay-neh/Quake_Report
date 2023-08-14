package com.example.quakereport.Network;

import com.example.quakereport.POJO.Earthquakes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetEarthquakes {
    @GET("/fdsnws/event/1/query?format=geojson&orderby=time&limit=50")
    Call<Earthquakes> getAllEarthquakes();
}
