package com.example.quakereport.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface EarthquakeDatabaseDao {

    //Testing RawQuery for Read all operation
    @RawQuery(observedEntities = Earthquake.class)
    LiveData<List<Earthquake>> getAllQuakes(SupportSQLiteQuery query);

    //Read single operation
    @Query("SELECT * FROM Earthquake WHERE id = :id")
    LiveData<Earthquake> getSingleQuakeData(int id);

    //Insert all operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuakes(List<Earthquake> quakeData);

    //Delete all operation
    @Query("DELETE FROM Earthquake")
    void deleteAll();
}
