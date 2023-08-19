package com.example.quakereport.ui.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quakereport.data.EarthquakeRepository;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
    private final EarthquakeRepository earthquakeRepository;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(application);
        refreshDataSource();
    }

    public void refreshDataSource() {
        earthquakeRepository.refreshDataSource();
    }

    public LiveData<List<OverviewUIState>> getOverViewUIStateList(String order, String limit){
        return earthquakeRepository.getDataSourceEntries(order, limit);
    }

    public LiveData<OverviewUIState> getOverViewUIStateById(int id) {
        return earthquakeRepository.getDataSourceEntryById(id);
    }

}
