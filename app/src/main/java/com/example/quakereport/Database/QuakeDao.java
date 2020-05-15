package com.example.quakereport.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface QuakeDao {
    //Read operation
    @Query("SELECT * FROM quake_data ORDER BY time ASC LIMIT:limit")
    LiveData<List<QuakeData>> getQuakeData(int limit);

    //Testing RawQuery
    @RawQuery(observedEntities = QuakeData.class)
    LiveData<List<QuakeData>> getRawQueryData(SupportSQLiteQuery query);

    //Read single operation
    @Query("SELECT * FROM quake_data WHERE id = :id")
    LiveData<QuakeData> getSingleQuakeData(int id);

    //Insert all operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuakes(List<QuakeData> quakeData);

    //Delete operation
    @Query("DELETE FROM quake_data")
    void deleteAll();
}
