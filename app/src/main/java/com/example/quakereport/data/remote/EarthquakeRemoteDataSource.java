package com.example.quakereport.data.remote;

import androidx.lifecycle.LiveData;

import com.example.quakereport.data.EarthquakeDataSource;
import com.example.quakereport.data.local.Earthquake;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EarthquakeRemoteDataSource implements EarthquakeDataSource {

    @Override
    public Observable<EarthquakeProperty> getEarthquakes() {
        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Observable<EarthquakeProperty> observable = service.getAllEarthquakes();
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public LiveData<List<Earthquake>> observeEarthquakes(String order, String limit) {
        // Not required for remote datasource
        return null;
    }

    @Override
    public LiveData<Earthquake> observeEarthquake(String eventId) {
        // Not required for remote datasource
        return null;
    }

    @Override
    public void saveEarthquakes(EarthquakeProperty earthquakeProperty) {
        // Not required for remote datasource
    }

    @Override
    public Completable deleteAllEarthquake() {
        // Not required for remote datasource
        return null;
    }
}
