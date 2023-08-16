package com.example.quakereport.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quakereport.data.database.Earthquake;
import com.example.quakereport.data.database.EarthquakeDatabase;
import com.example.quakereport.data.database.EarthquakeDatabaseDao;
import com.example.quakereport.data.network.EarthquakeProperty;
import com.example.quakereport.data.network.GetEarthquakes;
import com.example.quakereport.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeRepository {

    private final EarthquakeDatabaseDao earthquakeDatabaseDao;

    public EarthquakeRepository(Application application) {
        EarthquakeDatabase earthquakeDatabase = EarthquakeDatabase.getDatabase(application);
        earthquakeDatabaseDao = earthquakeDatabase.quakeDao();
    }

    //Get all entries from datasource
    public LiveData<List<Earthquake>> getDataSourceEntries(String order, String limit) {
        String statement = "SELECT * FROM earthquake ORDER BY " + order + " LIMIT " + limit;
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        return earthquakeDatabaseDao.getAllQuakes(query);
    }
    //Get single entry

    public LiveData<Earthquake> getDataSourceEntryById(int id) {
        return earthquakeDatabaseDao.getSingleQuakeData(id);
    }

    //Update local data entry with remote
    private void updateDataSource(List<Earthquake> data) {
        //deleteLocalData();
        insertAllData(data);
    }

    //Insert all operation
    private void insertAllData(final List<Earthquake> quakeData) {
        EarthquakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                earthquakeDatabaseDao.insertAllQuakes(quakeData);
            }
        });
    }

    //Delete operation
    private void deleteLocalData() {
        EarthquakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                earthquakeDatabaseDao.deleteAll();
            }
        });
    }

    // Get network Data
    public void syncDataSource(){
        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Call<EarthquakeProperty> call = service.getAllEarthquakes();
        List<Earthquake> data = new ArrayList<>();
        call.enqueue(new Callback<EarthquakeProperty>() {
            @Override
            public void onResponse(Call<EarthquakeProperty> call, Response<EarthquakeProperty> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().getFeatures().size(); i++) {
                        data.add(new Earthquake(response.body().getFeatures().get(i).getProperties().getEventId(),
                                response.body().getFeatures().get(i).getProperties().getMagnitude(),
                                response.body().getFeatures().get(i).getProperties().getPlace(),
                                response.body().getFeatures().get(i).getProperties().getTime()));
                    }
                    updateDataSource(data);
                }
            }

            @Override
            public void onFailure(Call<EarthquakeProperty> call, Throwable t) {
                Log.i("network Error response", t.toString());
            }
        });
    }

}
