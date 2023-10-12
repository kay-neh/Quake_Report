package com.example.quakereport.data.local;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quakereport.data.EarthquakeDataSource;
import com.example.quakereport.data.remote.EarthquakeProperty;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EarthquakeLocalDataSource implements EarthquakeDataSource {

    EarthquakeDao earthquakeDao;

    public EarthquakeLocalDataSource(EarthquakeDao earthquakeDao) {
        this.earthquakeDao = earthquakeDao;
    }

    @Override
    public Observable<EarthquakeProperty> getEarthquakes() {
        // Not Required for local datasource
        return null;
    }

    @Override
    public LiveData<List<Earthquake>> observeEarthquakes(String order, String limit) {
        String statement = "SELECT * FROM earthquake ORDER BY " + order + " LIMIT " + limit;
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        return earthquakeDao.getAllQuakes(query);
    }

    @Override
    public LiveData<Earthquake> observeEarthquake(String eventId) {
        return earthquakeDao.getSingleQuakeData(eventId);
    }

    @Override
    public void saveEarthquakes(EarthquakeProperty earthquakeProperty) {
        earthquakeDao.insertAllQuakes(earthquakeProperty.asDatabaseModel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public Completable deleteAllEarthquake() {
        return earthquakeDao.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
