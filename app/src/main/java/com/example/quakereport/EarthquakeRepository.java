package com.example.quakereport;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quakereport.Database.QuakeDao;
import com.example.quakereport.Database.QuakeData;
import com.example.quakereport.Database.QuakeDatabase;
import com.example.quakereport.Network.GetEarthquakes;
import com.example.quakereport.Network.RetrofitClient;
import com.example.quakereport.POJO.Earthquakes;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class EarthquakeRepository {

    private QuakeDao quakeDao;


    public EarthquakeRepository(Application application) {
        QuakeDatabase quakeDatabase = QuakeDatabase.getDatabase(application);
        quakeDao = quakeDatabase.quakeDao();
    }

     //Testing rawQuery Read all operation from roomDb
     public LiveData<List<QuakeData>> getAllRoomQuake(String order,String limit){
         String statement = "SELECT * FROM quake_data ORDER BY " + order + " LIMIT " + limit;
         SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
         return quakeDao.getRawQueryData(query);
     }

    //Reading Api response from USGS and
    //Store server response in roomDB
    public void updateRoomDb(Context context,SwipeRefreshLayout swipe){


        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Call<Earthquakes> call = service.getAllEarthquakes("geojson","time","100");

        call.enqueue(new Callback<Earthquakes>() {
            @Override
            public void onResponse(Call<Earthquakes> call, Response<Earthquakes> response) {
                List<QuakeData> data = new ArrayList<>();
                if (response.body() != null) {
                    for (int i = 0; i < response.body().getFeatures().size(); i++) {
                        data.add(new QuakeData(response.body().getFeatures().get(i).getProperties().getEventId(),
                                response.body().getFeatures().get(i).getProperties().getMagnitude(),
                                response.body().getFeatures().get(i).getProperties().getPlace(),
                                response.body().getFeatures().get(i).getProperties().getTime()));
                    }
                    insertAllQuake(data);
                }
            }

            @Override
            public void onFailure(Call<Earthquakes> call, Throwable t) {
                Snackbar.make(swipe, "Unable to fetch new data", Snackbar.LENGTH_SHORT)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Respond to the click, such as by undoing the modification that caused
                                // this message to be displayed
                                updateRoomDb(context,swipe);
                            }
                        }).setActionTextColor(context.getColor(R.color.colorAccent))
                        .setBackgroundTint(context.getColor(R.color.snackBarColor))
                        .setTextColor(context.getColor(R.color.snackBarTextColor))
                        .show();
                //Toast.makeText(context,"Unable to fetch new data",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Insert all operation
    private void insertAllQuake(final List<QuakeData> quakeData){
        QuakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quakeDao.insertAllQuakes(quakeData);
            }
        });
    }

    //Delete operation
    private void clearRoomDb(){
        QuakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quakeDao.deleteAll();
            }
        });
    }

     //Read single operation
     //scheduled for later implementation
     public LiveData<QuakeData> getSingleRoomQuake(final int id){
         return quakeDao.getSingleQuakeData(id);
     }

//    //Read all operation from roomDb
//    public LiveData<List<QuakeData>> getAllRoomQuake(String order,int limit){
//        return quakeDao.getQuakeData( limit);
//    }
}
