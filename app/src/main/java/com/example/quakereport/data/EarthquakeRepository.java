package com.example.quakereport.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.quakereport.data.database.Earthquake;
import com.example.quakereport.data.database.EarthquakeDatabase;
import com.example.quakereport.data.network.EarthquakeProperty;
import com.example.quakereport.data.network.GetEarthquakes;
import com.example.quakereport.data.network.RetrofitClient;
import com.example.quakereport.ui.details.DetailsUIState;
import com.example.quakereport.ui.overview.OverviewUIState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        return Transformations.map(f, this::asOverViewUIStateList);
    }

    //Get single entry
    public LiveData<DetailsUIState> getDataSourceEntryById(String eventId) {
        LiveData<Earthquake> f = database.quakeDao().getSingleQuakeData(eventId);
        return Transformations.map(f, this::asDetailsUIState);
    }

    private DetailsUIState asDetailsUIState(Earthquake earthquake){
        return new DetailsUIState(earthquake.getEventId(), earthquake.getMagnitude(), earthquake.getPlace(), earthquake.getTime(), earthquake.getUrl(), earthquake.getLongitude(), earthquake.getLatitude(), earthquake.getDepth());
    }

    private List<OverviewUIState> asOverViewUIStateList(List<Earthquake> earthquakeList){
        List<OverviewUIState> overviewUIStateList = new ArrayList<>();
        for(Earthquake e: earthquakeList){
            overviewUIStateList.add(new OverviewUIState(e.getEventId(), e.getMagnitude(), e.getPlace(), e.getTime()));
        }
        return overviewUIStateList;
    }

    //Insert all operation
    private void insertAllData(EarthquakeProperty earthquakeProperty) {
        database.quakeDao().insertAllQuakes(earthquakeProperty.asDatabaseModel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //Delete operation
    private void deleteLocalData() {
        database.quakeDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    // Gets the Network Response as an RXJava Observable
    private @NonNull Observable<EarthquakeProperty> getObservable(){
        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Observable<EarthquakeProperty> observable = service.getAllEarthquakes();
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    // Refresh Data is called on startup
    public void refreshDataSource() {
        getObservable().subscribe(new Observer<EarthquakeProperty>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull EarthquakeProperty earthquakeProperty) {
                insertAllData(earthquakeProperty);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("network Error response", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //Sync data periodically
    public void syncData(){
        getObservable().subscribe(new Observer<EarthquakeProperty>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull EarthquakeProperty earthquakeProperty) {
                deleteLocalData();
                insertAllData(earthquakeProperty);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("network Error response", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
