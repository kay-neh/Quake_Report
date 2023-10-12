package com.example.quakereport.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.quakereport.data.local.Earthquake;
import com.example.quakereport.data.local.EarthquakeDatabase;
import com.example.quakereport.data.local.EarthquakeLocalDataSource;
import com.example.quakereport.data.remote.EarthquakeProperty;
import com.example.quakereport.data.remote.EarthquakeRemoteDataSource;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class EarthquakeRepository {

    private final EarthquakeDataSource earthquakeLocalDataSource;
    private final EarthquakeDataSource earthquakeRemoteDataSource;

    public EarthquakeRepository(Application application) {
        EarthquakeDatabase database = EarthquakeDatabase.getDatabase(application);
        earthquakeLocalDataSource = new EarthquakeLocalDataSource(database.earthquakeDao());
        earthquakeRemoteDataSource = new EarthquakeRemoteDataSource();
    }

    public LiveData<List<Earthquake>> getEarthquakes(boolean forceUpdate, String order, String limit){
        if(forceUpdate){
            updateEarthquakeFromRemoteDataSource();
        }
        return earthquakeLocalDataSource.observeEarthquakes(order, limit);
    }

    public LiveData<Earthquake> getEarthquake(String eventId){
        return earthquakeLocalDataSource.observeEarthquake(eventId);
    }

    public void updateEarthquakeFromRemoteDataSource(){
        Observable<EarthquakeProperty> observableRemoteEarthquakes = earthquakeRemoteDataSource.getEarthquakes();
        observableRemoteEarthquakes.subscribe(new Observer<EarthquakeProperty>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull EarthquakeProperty earthquakeProperty) {
                Log.e("OnNext called", "onNext");
                earthquakeLocalDataSource.deleteAllEarthquake()
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                // save to database only when delete operation is completed
                                earthquakeLocalDataSource.saveEarthquakes(earthquakeProperty);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("remote Error response", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
