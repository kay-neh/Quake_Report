package com.example.quakereport;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.quakereport.data.EarthquakeRepository;


import retrofit2.HttpException;

public class SyncDataWork extends Worker {

    static final String WORK_NAME = "SyncDataWorker";

    public SyncDataWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        EarthquakeRepository repository = new EarthquakeRepository((Application) getApplicationContext());
        try{
            repository.refreshEarthquakes();
            return Result.success();
        }catch (HttpException e){
            return Result.retry();
        }
    }



}
