package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.quakereport.Database.QuakeData;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    RecyclerView earthquakeRecyclerView;
    View emptyView;
    ProgressBar loading;
    SwipeRefreshLayout swipe;
    MaterialButton refresh;
    EarthquakeAdapter adapter;
    SharedPreferences sharedPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener spListen;
    EarthquakeViewModel earthquakeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        //Init sharedPreference
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean nightValue = sharedPrefs.getBoolean(
                getString(R.string.settings_dark_mode_key),
                getResources().getBoolean(R.bool.settings_dark_mode_defult));

        setNightMode(nightValue);

        //Init viewModel
        earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);

        //Initialize all ui components
        earthquakeRecyclerView = findViewById(R.id.list);
        emptyView = findViewById(R.id.new_empty_test);
        swipe = findViewById(R.id.swipe_down);
        refresh = findViewById(R.id.refresh_button);
        loading = findViewById(R.id.progress_bar);
        initAdapter();

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String limit = sharedPrefs.getString(
                getString(R.string.settings_limit_key),
                getString(R.string.settings_limit_default));

        //Using the ViewModel to query the repository
        earthquakeViewModel.updateRoomDb(EarthquakeActivity.this,swipe);
        getRoomData(orderBy,limit);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                emptyView.setVisibility(View.GONE);
                swipe.setRefreshing(false);
                earthquakeViewModel.updateRoomDb(EarthquakeActivity.this,swipe);
                getRoomData(orderBy,limit);
            }
        });

        //OnSharedPrefChange listener object
        spListen = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals(getString(R.string.settings_order_by_key))){
                    getRoomData(sharedPreferences.getString(key,getString(R.string.settings_order_by_default))
                            ,
                            sharedPreferences.getString(
                            getString(R.string.settings_limit_key),
                            getString(R.string.settings_limit_default)));
                }
                if(key.equals(getString(R.string.settings_limit_key))){
                    getRoomData(sharedPreferences.getString(getString(R.string.settings_order_by_key),
                            getString(R.string.settings_order_by_default))
                            ,
                            sharedPreferences.getString(key,getString(R.string.settings_limit_default)));
                }
                if(key.equals(getString(R.string.settings_dark_mode_key))){
                    setNightMode(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.settings_dark_mode_defult)));
                }
            }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(spListen);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyView.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                earthquakeViewModel.updateRoomDb(EarthquakeActivity.this,swipe);

            }
        });
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        earthquakeRecyclerView.setLayoutManager(llm);
        adapter = new EarthquakeAdapter(this, new EarthquakeAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int index) {
                //handle click events here
            }
        });
//        loading.setVisibility(View.GONE);
        earthquakeRecyclerView.setAdapter(adapter);
    }

    public void getRoomData(String orderBy,String limit){
        earthquakeViewModel.getAllRoomQuakes(orderBy,limit).observe(this, new Observer<List<QuakeData>>() {
            @Override
            public void onChanged(List<QuakeData> quakeData) {
                if(quakeData != null) {
                    loading.setVisibility(View.GONE);
                    adapter.setQuakes(quakeData);
                    if(!quakeData.isEmpty()){
                        emptyView.setVisibility(View.GONE);
                    }else{
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void setNightMode(Boolean nightValue){
        if(nightValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(spListen);
    }
}
