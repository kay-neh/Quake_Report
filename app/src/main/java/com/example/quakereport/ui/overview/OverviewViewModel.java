package com.example.quakereport.ui.overview;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.quakereport.R;
import com.example.quakereport.data.EarthquakeRepository;
import com.example.quakereport.data.database.EarthquakeDatabase;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
    private final EarthquakeRepository earthquakeRepository;
    SharedPreferences sharedPrefs;
    String orderFilter, limitFilter;
    LiveData<List<OverviewUIState>> overviewUIStateList;

    private MutableLiveData<String[]> _navigateToEarthquakeDetails = new MutableLiveData<>();
    LiveData<String[]> navigateToEarthquakeDetails = _navigateToEarthquakeDetails;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        EarthquakeDatabase database = EarthquakeDatabase.getDatabase(application);
        earthquakeRepository = new EarthquakeRepository(database);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application);
        orderFilter = sharedPrefs.getString(
                application.getString(R.string.settings_order_by_key),
                application.getString(R.string.settings_order_by_default));
        limitFilter = sharedPrefs.getString(
                application.getString(R.string.settings_limit_key),
                application.getString(R.string.settings_limit_default));
        refreshDataSource();
        getOverViewUIStateList(orderFilter,limitFilter);
    }

    public void onEarthquakeClicked(String[] data){
        _navigateToEarthquakeDetails.setValue(data);
    }

    public void onEarthquakeDetailsNavigated() {
        _navigateToEarthquakeDetails.setValue(null);
    }

    public void refreshDataSource() {
        earthquakeRepository.refreshDataSource();
    }

    public void getOverViewUIStateList(String order, String limit){
        overviewUIStateList =  earthquakeRepository.getDataSourceEntries(order, limit);
    }

}
