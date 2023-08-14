package com.example.quakereport.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface QuakeDao {

    //Testing RawQuery for Read all operation
    @RawQuery(observedEntities = QuakeData.class)
    LiveData<List<QuakeData>> getAllQuakes(SupportSQLiteQuery query);

    //Read single operation
    @Query("SELECT * FROM quake_data WHERE id = :id")
    LiveData<QuakeData> getSingleQuakeData(int id);

    //Insert all operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuakes(List<QuakeData> quakeData);

    //Delete all operation
    @Query("DELETE FROM quake_data")
    void deleteAll();
}
