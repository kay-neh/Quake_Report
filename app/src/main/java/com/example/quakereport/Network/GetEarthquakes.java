package com.example.quakereport.Network;

import com.example.quakereport.POJO.Earthquakes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetEarthquakes {
    @GET("/fdsnws/event/1/query?")
    Call<Earthquakes> getAllEarthquakes(@Query("format") String format,
                                        @Query("orderby") String orderBy,
                                        @Query("limit") String limit
    );
}
