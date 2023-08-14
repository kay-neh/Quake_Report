package com.example.quakereport;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quakereport.Database.QuakeData;

import java.util.List;

public class EarthquakeViewModel extends AndroidViewModel {

    private EarthquakeRepository earthquakeRepository;

    public EarthquakeViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(application);
    }

    public void syncDataSource() {
        earthquakeRepository.syncDataSource();
    }

    public LiveData<List<QuakeData>> getAllDataEntries(String order, String limit) {
        return earthquakeRepository.getDataSourceEntries(order, limit);
    }

    public LiveData<QuakeData> getDataSourceEntryById(int id) {
        return earthquakeRepository.getDataSourceEntryById(id);
    }

}
