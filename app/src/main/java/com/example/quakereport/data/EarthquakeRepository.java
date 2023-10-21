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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EarthquakeRepository {

    private final EarthquakeDataSource earthquakeLocalDataSource;
    private final EarthquakeDataSource earthquakeRemoteDataSource;

    public EarthquakeRepository(Application application) {
        EarthquakeDatabase database = EarthquakeDatabase.getDatabase(application);
        earthquakeLocalDataSource = new EarthquakeLocalDataSource(database.earthquakeDao());
        earthquakeRemoteDataSource = new EarthquakeRemoteDataSource();
    }

    public Single<List<Earthquake>> getEarthquakes(boolean forceUpdate, String order, String limit){
        if(forceUpdate){
            updateEarthquakesFromRemoteDataSource();
        }
        return earthquakeLocalDataSource.getEarthquakes(order, limit);
    }

    public Single<Earthquake> getEarthquake(boolean forceUpdate, String eventId){
        if(forceUpdate){
            updateEarthquakeFromRemoteDataSource();
        }
        return earthquakeLocalDataSource.getEarthquake(eventId);
    }

    public LiveData<List<Earthquake>> observeEarthquakes(String order, String limit){
        return earthquakeLocalDataSource.observeEarthquakes(order, limit);
    }

    public LiveData<Earthquake> observeEarthquake(String eventId){
        return earthquakeLocalDataSource.observeEarthquake(eventId);
    }

    public void refreshEarthquakes(){
        updateEarthquakesFromRemoteDataSource();
    }

    public void refreshEarthquake(){
        updateEarthquakeFromRemoteDataSource();
    }

    private void updateEarthquakesFromRemoteDataSource(){
        earthquakeRemoteDataSource.getEarthquakes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EarthquakeProperty>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull EarthquakeProperty earthquakeProperty) {
                Log.e("OnNext called", "onNext");
                syncEarthquakes(earthquakeProperty);
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

    private void updateEarthquakeFromRemoteDataSource(){
        // no implementation yet
    }

    private void syncEarthquakes(EarthquakeProperty earthquakeProperty){
        earthquakeLocalDataSource.deleteAllEarthquake()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        // save to database only when delete operation is completed
                        saveEarthquake(earthquakeProperty);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void saveEarthquake(EarthquakeProperty earthquakeProperty){
        earthquakeLocalDataSource.saveEarthquakes(earthquakeProperty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
