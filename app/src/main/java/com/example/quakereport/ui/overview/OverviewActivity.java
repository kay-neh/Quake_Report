package com.example.quakereport.ui.overview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.quakereport.R;
import com.example.quakereport.databinding.ActivityOverviewBinding;
import com.example.quakereport.ui.settings.SettingsActivity;
import com.google.android.material.snackbar.Snackbar;

public class OverviewActivity extends AppCompatActivity {

    ActivityOverviewBinding binding;
    OverviewAdapter adapter;
    SharedPreferences sharedPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener spListen;
    OverviewViewModel overviewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_overview);

        //Init viewModel
        overviewViewModel = new ViewModelProvider(this).get(OverviewViewModel.class);

        //Initialize all ui components
        initAdapter();

        //Set swipe down action
        swipeDownAction();

        //Init sharedPreference, get list and register listener
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setNightModeWithPreference();
        loadEarthquakes(sharedPrefs.getString(
                        getString(R.string.settings_order_by_key),
                        getString(R.string.settings_order_by_default)),
                sharedPrefs.getString(
                        getString(R.string.settings_limit_key),
                        getString(R.string.settings_limit_default)));
        setPreferenceListener();
        sharedPrefs.registerOnSharedPreferenceChangeListener(spListen);

    }


    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.list.setLayoutManager(llm);
        adapter = new OverviewAdapter(eventId -> {
            //handle click events here
            Log.i("Adapter Clicked",eventId);
        });
        binding.list.setAdapter(adapter);
    }

    public void swipeDownAction(){
        binding.swipeDown.setOnRefreshListener(() -> {
            binding.newEmptyTest.setVisibility(View.GONE);
            overviewViewModel.refreshDataSource();
            binding.swipeDown.setRefreshing(false);
            Snackbar.make(binding.swipeDown, "Sync Successful", Snackbar.LENGTH_SHORT)
//                    .setAction("Retry", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            updateRoomDb(context, swipe);
//                        }
//                    })
//                    .setActionTextColor(getColor(R.color.colorAccent))
                    .setBackgroundTint(getColor(R.color.snackBarColor))
                    .setTextColor(getColor(R.color.snackBarTextColor))
                    .show();
        });
    }

    public void setNightModeWithPreference(){
        //Set Night mode
        setNightMode(sharedPrefs.getBoolean(
                getString(R.string.settings_dark_mode_key),
                getResources().getBoolean(R.bool.settings_dark_mode_defult)));
    }

    public void setNightMode(Boolean nightValue) {
        if (nightValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void loadEarthquakes(String orderBy, String limit) {
        overviewViewModel.getOverViewUIStateList(orderBy, limit).observe(this, overviewUIStateList -> {
            if (overviewUIStateList != null) {
                //loading.setVisibility(View.GONE);
                Log.i("StateList", String.valueOf(overviewUIStateList.size()));
                adapter.submitList(overviewUIStateList);
                if (!overviewUIStateList.isEmpty()) {
                    // emptyView.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setPreferenceListener(){
        //Set up OnSharedPrefChange listener object
        spListen = (sharedPreferences, key) -> {
            if (key.equals(getString(R.string.settings_order_by_key))) {
                loadEarthquakes(sharedPreferences.getString(key, getString(R.string.settings_order_by_default))
                        ,
                        sharedPreferences.getString(
                                getString(R.string.settings_limit_key),
                                getString(R.string.settings_limit_default)));
            }
            if (key.equals(getString(R.string.settings_limit_key))) {
                loadEarthquakes(sharedPreferences.getString(getString(R.string.settings_order_by_key),
                                getString(R.string.settings_order_by_default))
                        ,
                        sharedPreferences.getString(key, getString(R.string.settings_limit_default)));
            }
            if (key.equals(getString(R.string.settings_dark_mode_key))) {
                setNightMode(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.settings_dark_mode_defult)));
            }
        };
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
