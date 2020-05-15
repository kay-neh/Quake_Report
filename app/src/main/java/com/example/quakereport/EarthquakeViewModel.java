package com.example.quakereport;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quakereport.Database.QuakeData;

import java.util.List;

public class EarthquakeViewModel extends AndroidViewModel {

    private EarthquakeRepository earthquakeRepository;

    public EarthquakeViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(application);
    }

    public void updateRoomDb(Context context,SwipeRefreshLayout swipe){
        earthquakeRepository.updateRoomDb(context,swipe);
    }

    public LiveData<List<QuakeData>> getAllRoomQuakes(String order, String limit){
        return earthquakeRepository.getAllRoomQuake(order, limit);
    }
}
