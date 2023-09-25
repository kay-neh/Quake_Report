package com.example.quakereport.ui.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quakereport.data.EarthquakeRepository;
import com.example.quakereport.data.database.EarthquakeDatabase;

public class DetailsViewModel extends AndroidViewModel {

    private final EarthquakeRepository earthquakeRepository;
    String eventId;
    LiveData<DetailsUIState> detailsUIState;

    public DetailsViewModel(String eventId, @NonNull Application application) {
        super(application);
        EarthquakeDatabase database = EarthquakeDatabase.getDatabase(application);
        earthquakeRepository = new EarthquakeRepository(database);
        this.eventId = eventId;
        getDetailsUIState(eventId);
    }

    public void getDetailsUIState(String eventId){
        detailsUIState =  earthquakeRepository.getDataSourceEntryById(eventId);
    }

}
