package com.example.quakereport.data;

import androidx.lifecycle.LiveData;

import com.example.quakereport.data.local.Earthquake;
import com.example.quakereport.data.remote.EarthquakeProperty;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface EarthquakeDataSource {

    public Observable<EarthquakeProperty> getEarthquakes();

    public LiveData<List<Earthquake>> observeEarthquakes(String order, String limit);

    public LiveData<Earthquake> observeEarthquake(String eventId);

    public Single<List<Earthquake>> getEarthquakes(String order, String limit);

    public Single<Earthquake> getEarthquake(String eventId);

    public Completable saveEarthquakes(EarthquakeProperty earthquakeProperty);

    public Completable deleteAllEarthquake();


}
