package com.example.quakereport.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {QuakeData.class}, version = 1, exportSchema = false)
public abstract class QuakeDatabase extends RoomDatabase {

    public abstract QuakeDao quakeDao();

    private static final String DATABASE_NAME = "quake_database";
    private static volatile QuakeDatabase INSTANCE;
    private static final int NO_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NO_OF_THREADS);

    public static QuakeDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (QuakeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), QuakeDatabase.class,
                            QuakeDatabase.DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
