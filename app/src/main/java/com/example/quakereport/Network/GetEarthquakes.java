package com.example.quakereport.Network;

import com.example.quakereport.POJO.Earthquakes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetEarthquakes {
    @GET("/fdsnws/event/1/query?")
    Call<Earthquakes> getAllEarthquakes(@Query("format") String format,
                                              @Query("limit") String limit,
                                              @Query("minmag") String minMagnitude,
                                              @Query("orderby") String orderBy);
}
