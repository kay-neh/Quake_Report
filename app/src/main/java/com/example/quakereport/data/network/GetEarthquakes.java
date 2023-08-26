package com.example.quakereport.data.network;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface GetEarthquakes {
    @GET("/fdsnws/event/1/query?format=geojson&orderby=time&limit=50")
    Observable<EarthquakeProperty> getAllEarthquakes();
}
