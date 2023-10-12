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

@Dao
public interface EarthquakeDao {

    //Testing RawQuery for Read all operation
    @RawQuery(observedEntities = Earthquake.class)
    LiveData<List<Earthquake>> getAllQuakes(SupportSQLiteQuery query);

    //Read single operation
    @Query("SELECT * FROM earthquake WHERE eventId = :eventId")
    LiveData<Earthquake> getSingleQuakeData(String eventId);

    //Insert all operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllQuakes(List<Earthquake> earthquakeList);

    //Delete all operation
    @Query("DELETE FROM earthquake")
    Completable deleteAll();
}
