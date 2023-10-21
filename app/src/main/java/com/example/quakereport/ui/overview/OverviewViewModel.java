package com.example.quakereport.ui.overview;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.example.quakereport.R;
import com.example.quakereport.data.EarthquakeRepository;
import com.example.quakereport.data.local.Earthquake;

import java.util.ArrayList;
import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
    private final EarthquakeRepository earthquakeRepository;
    SharedPreferences sharedPrefs;
    String orderFilter, limitFilter;
    LiveData<List<OverviewUIState>> items;

    private MutableLiveData<Boolean> _forceUpdate = new MutableLiveData<>(false);

    private MutableLiveData<String[]> _navigateToEarthquakeDetails = new MutableLiveData<>();
    LiveData<String[]> navigateToEarthquakeDetails = _navigateToEarthquakeDetails;

    private MutableLiveData<Boolean> _snackBarEvent = new MutableLiveData<>(false);
    LiveData<Boolean> snackBarEvent = _snackBarEvent;

    private MutableLiveData<Boolean> _progressBarEvent = new MutableLiveData<>(false);
    LiveData<Boolean> progressBarEvent = _progressBarEvent;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(application);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application);
        orderFilter = sharedPrefs.getString(
                application.getString(R.string.settings_order_by_key),
                application.getString(R.string.settings_order_by_default));
        limitFilter = sharedPrefs.getString(
                application.getString(R.string.settings_limit_key),
                application.getString(R.string.settings_limit_default));
        loadEarthquakes(true,orderFilter,limitFilter);
    }

    public void onEarthquakeClicked(String[] data){
        _navigateToEarthquakeDetails.setValue(data);
    }

    public void onEarthquakeDetailsNavigated() {
        _navigateToEarthquakeDetails.setValue(null);
    }

    public void triggerSnackBar(){
        _snackBarEvent.setValue(true);
    }

    public void onSnackBarTriggered(){
        _snackBarEvent.setValue(false);
    }

    public void triggerProgressBar(){
        _progressBarEvent.setValue(true);
    }

    public void onProgressBarTriggered(){
        _progressBarEvent.setValue(false);
    }

    private List<OverviewUIState> asOverViewUIStateList(List<Earthquake> earthquakeList){
        List<OverviewUIState> overviewUIStateList = new ArrayList<>();
        for(Earthquake e: earthquakeList){
            overviewUIStateList.add(new OverviewUIState(e.getEventId(), e.getMagnitude(), e.getPlace(), e.getTime()));
        }
        return overviewUIStateList;
    }

    public void loadEarthquakes(boolean forceUpdate, String order, String limit){
        if(forceUpdate){
            triggerProgressBar();
            earthquakeRepository.refreshEarthquakes();
        }
        items = Transformations.map(earthquakeRepository.observeEarthquakes(order, limit), this::asOverViewUIStateList);
    }

//    public void loadEarthquakes(boolean forceUpdate){
//        _forceUpdate.setValue(forceUpdate);
//    }

    public void refreshEarthquakes(){
        earthquakeRepository.refreshEarthquakes();
    }

}
