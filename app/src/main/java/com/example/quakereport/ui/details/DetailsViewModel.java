package com.example.quakereport.ui.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.quakereport.data.EarthquakeRepository;
import com.example.quakereport.data.local.Earthquake;

public class DetailsViewModel extends AndroidViewModel {

    private final EarthquakeRepository earthquakeRepository;
    String eventId;
    public LiveData<DetailsUIState> detailsUIState;

    public DetailsViewModel(String eventId, @NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(application);
        this.eventId = eventId;
        getDetailsUIState(eventId);
    }

    private void getDetailsUIState(String eventId){
        detailsUIState = Transformations.map(earthquakeRepository.observeEarthquake(eventId), this::asDetailsUIState);
    }

    private DetailsUIState asDetailsUIState(Earthquake earthquake){
        return new DetailsUIState(earthquake.getMagnitude(), earthquake.getPlace(), earthquake.getTime(), earthquake.getUrl(), earthquake.getLongitude(), earthquake.getLatitude(), earthquake.getDepth());
    }

}
