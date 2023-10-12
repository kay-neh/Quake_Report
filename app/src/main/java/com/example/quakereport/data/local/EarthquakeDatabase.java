package com.example.quakereport.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Earthquake.class}, version = 1, exportSchema = false)
public abstract class EarthquakeDatabase extends RoomDatabase {

    public abstract EarthquakeDao earthquakeDao();

    private static final String DATABASE_NAME = "quake_database";
    private static volatile EarthquakeDatabase INSTANCE;

    public static EarthquakeDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (EarthquakeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EarthquakeDatabase.class,
                            EarthquakeDatabase.DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
