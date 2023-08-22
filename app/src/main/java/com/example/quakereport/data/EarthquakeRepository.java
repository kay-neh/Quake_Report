package com.example.quakereport.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quakereport.data.database.Earthquake;
import com.example.quakereport.data.database.EarthquakeDatabase;
import com.example.quakereport.data.database.EarthquakeDatabaseDao;
import com.example.quakereport.data.network.EarthquakeProperty;
import com.example.quakereport.data.network.GetEarthquakes;
import com.example.quakereport.data.network.RetrofitClient;
import com.example.quakereport.ui.overview.OverviewUIState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeRepository {

    private final EarthquakeDatabase database;

    public EarthquakeRepository(EarthquakeDatabase database) {
        this.database = database;
    }

    //Get all entries from datasource
    public LiveData<List<OverviewUIState>> getDataSourceEntries(String order, String limit) {
        String statement = "SELECT * FROM earthquake ORDER BY " + order + " LIMIT " + limit;
        SupportSQLiteQuery query = new SimpleSQLiteQuery(statement, new Object[]{});
        LiveData<List<Earthquake>> f = database.quakeDao().getAllQuakes(query);
        return Transformations.map(f,g->asUIModelList(g));
    }

    //Get single entry
    public LiveData<OverviewUIState> getDataSourceEntryById(int id) {
        LiveData<Earthquake> f = database.quakeDao().getSingleQuakeData(id);
        return Transformations.map(f,g->asUIModel(g));
    }

    public OverviewUIState asUIModel(Earthquake earthquake){
        return new OverviewUIState(earthquake.getEventId(), earthquake.getMagnitude(), earthquake.getPlace(), earthquake.getTime());
    }

    private List<OverviewUIState> asUIModelList(List<Earthquake> earthquakeList){
        List<OverviewUIState> overviewUIStateList = new ArrayList<>();
        for(Earthquake e: earthquakeList){
            overviewUIStateList.add(asUIModel(e));
        }
        return overviewUIStateList;
    }

    //Insert all operation
    private void insertAllData(final List<Earthquake> earthquakeList) {
        EarthquakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.quakeDao().insertAllQuakes(earthquakeList);
            }
        });
    }

    //Delete operation
    private void deleteLocalData() {
        EarthquakeDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.quakeDao().deleteAll();
            }
        });
    }

    //Sync data periodically
    public void syncData(){
        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Call<EarthquakeProperty> call = service.getAllEarthquakes();
        call.enqueue(new Callback<EarthquakeProperty>() {
            @Override
            public void onResponse(Call<EarthquakeProperty> call, Response<EarthquakeProperty> response) {
                if (response.body() != null) {
                    deleteLocalData();
                    insertAllData(response.body().asDatabaseModel());
                }
            }

            @Override
            public void onFailure(Call<EarthquakeProperty> call, Throwable t) {
                Log.i("network Error response", t.toString());
            }
        });
    }

    // Get network Data
    public void refreshDataSource(){
        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Call<EarthquakeProperty> call = service.getAllEarthquakes();
        call.enqueue(new Callback<EarthquakeProperty>() {
            @Override
            public void onResponse(Call<EarthquakeProperty> call, Response<EarthquakeProperty> response) {
                if (response.body() != null) {
                    insertAllData(response.body().asDatabaseModel());
                }
            }

            @Override
            public void onFailure(Call<EarthquakeProperty> call, Throwable t) {
                Log.i("network Error response", t.toString());
            }
        });
    }

}
