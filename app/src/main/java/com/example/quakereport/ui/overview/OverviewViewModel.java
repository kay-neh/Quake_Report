package com.example.quakereport.ui.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quakereport.data.database.Earthquake;
import com.example.quakereport.data.EarthquakeRepository;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
    private final EarthquakeRepository earthquakeRepository;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(application);
        syncDataSource();
    }

    public void syncDataSource() {
        earthquakeRepository.syncDataSource();
    }

    public LiveData<List<Earthquake>> getEarthquakes(String order, String limit){
        return earthquakeRepository.getDataSourceEntries(order, limit);
    }

    public LiveData<Earthquake> getEarthquakeById(int id) {
        return earthquakeRepository.getDataSourceEntryById(id);
    }

}
