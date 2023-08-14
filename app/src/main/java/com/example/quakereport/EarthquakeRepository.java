package com.example.quakereport;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quakereport.Database.QuakeDao;
import com.example.quakereport.Database.QuakeData;
import com.example.quakereport.Database.QuakeDatabase;
import com.example.quakereport.Network.GetEarthquakes;
import com.example.quakereport.Network.RetrofitClient;
import com.example.quakereport.POJO.Earthquakes;

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

    //Get all entries from datasource
    public LiveData<List<QuakeData>> getDataSourceEntries(String order, String limit) {
        String statement = "SELECT * FROM quake_data ORDER BY " + order + " LIMIT " + limit;
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        return quakeDao.getAllQuakes(query);
    }
    //Get single entry

    public LiveData<QuakeData> getDataSourceEntryById(int id) {
        return quakeDao.getSingleQuakeData(id);
    }

    //Update local data entry with remote
    private void updateDataSource(List<QuakeData> data) {
        //deleteLocalData();
        insertAllData(data);
    }

    //Insert all operation
    private void insertAllData(final List<QuakeData> quakeData) {
        QuakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quakeDao.insertAllQuakes(quakeData);
            }
        });
    }

    //Delete operation
    private void deleteLocalData() {
        QuakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                quakeDao.deleteAll();
            }
        });
    }

    // Get Network Data
    public void syncDataSource(){
        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Call<Earthquakes> call = service.getAllEarthquakes();
        List<QuakeData> data = new ArrayList<>();
        call.enqueue(new Callback<Earthquakes>() {
            @Override
            public void onResponse(Call<Earthquakes> call, Response<Earthquakes> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().getFeatures().size(); i++) {
                        data.add(new QuakeData(response.body().getFeatures().get(i).getProperties().getEventId(),
                                response.body().getFeatures().get(i).getProperties().getMagnitude(),
                                response.body().getFeatures().get(i).getProperties().getPlace(),
                                response.body().getFeatures().get(i).getProperties().getTime()));
                    }
                    updateDataSource(data);
                }
            }

            @Override
            public void onFailure(Call<Earthquakes> call, Throwable t) {
                Log.i("Network Error response", t.toString());
            }
        });
    }

}
