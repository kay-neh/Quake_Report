package com.example.quakereport;

import android.app.Application;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class QuakeReportApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupRecurringWork();
    }

    private void setupRecurringWork() {
//
//        boolean idle = false;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            idle = true;
//        }

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
              //  .setRequiresDeviceIdle(idle)
                .build();

        PeriodicWorkRequest repeatingRequest = new PeriodicWorkRequest.Builder(SyncDataWork.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork(
                SyncDataWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest);
    }


}
