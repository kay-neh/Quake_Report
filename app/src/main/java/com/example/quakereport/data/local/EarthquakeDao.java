package com.example.quakereport.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface EarthquakeDao {

    //Testing RawQuery for Observable Read all operation
    @RawQuery(observedEntities = Earthquake.class)
    LiveData<List<Earthquake>> observeEarthquakes(SupportSQLiteQuery query);

    //Observable Read single operation
    @Query("SELECT * FROM earthquake WHERE eventId = :eventId")
    LiveData<Earthquake> observeEarthquake(String eventId);

    //Testing RawQuery for Read all operation
    @RawQuery(observedEntities = Earthquake.class)
    Single<List<Earthquake>> getEarthquakes(SupportSQLiteQuery query);

    //Read single operation
    @Query("SELECT * FROM earthquake WHERE eventId = :eventId")
    Single<Earthquake> getEarthquake(String eventId);

    //Insert all operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertEarthquakes(List<Earthquake> earthquakeList);

    //Delete all operation
    @Query("DELETE FROM earthquake")
    Completable deleteEarthquakes();
}
