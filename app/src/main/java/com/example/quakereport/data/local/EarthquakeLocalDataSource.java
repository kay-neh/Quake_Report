package com.example.quakereport.data.local;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quakereport.data.EarthquakeDataSource;
import com.example.quakereport.data.remote.EarthquakeProperty;

import java.util.List;


import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


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
        return earthquakeDao.observeEarthquakes(query);
    }

    @Override
    public LiveData<Earthquake> observeEarthquake(String eventId) {
        return earthquakeDao.observeEarthquake(eventId);
    }

    @Override
    public Single<List<Earthquake>> getEarthquakes(String order, String limit) {
        String statement = "SELECT * FROM earthquake ORDER BY " + order + " LIMIT " + limit;
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        return earthquakeDao.getEarthquakes(query);
    }

    @Override
    public Single<Earthquake> getEarthquake(String eventId) {
        return earthquakeDao.getEarthquake(eventId);
    }

    @Override
    public Completable saveEarthquakes(EarthquakeProperty earthquakeProperty) {
        return earthquakeDao.insertEarthquakes(earthquakeProperty.asDatabaseModel());
    }

    @Override
    public Completable deleteAllEarthquake() {
        return earthquakeDao.deleteEarthquakes();
    }
}
