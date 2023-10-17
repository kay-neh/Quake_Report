package com.example.quakereport.data;

import androidx.lifecycle.LiveData;

import com.example.quakereport.data.local.Earthquake;
import com.example.quakereport.data.remote.EarthquakeProperty;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface EarthquakeDataSource {

    public Observable<EarthquakeProperty> getEarthquakes();

    public LiveData<List<Earthquake>> observeEarthquakes(String order, String limit);

    public LiveData<Earthquake> observeEarthquake(String eventId);

    public Completable saveEarthquakes(EarthquakeProperty earthquakeProperty);

    public Completable deleteAllEarthquake();


}
