package com.example.quakereport.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.quakereport.R;
import com.example.quakereport.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    SharedPreferences sharedPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        //Init sharedPreference, get list and register listener
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setNightModeWithPreference();
        onSharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(getString(R.string.settings_dark_mode_key))) {
                    setNightMode(sharedPreferences.getBoolean(key,getResources().getBoolean(R.bool.settings_dark_mode_default)));
                }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void setNightModeWithPreference(){
        //Set Night mode
        setNightMode(sharedPrefs.getBoolean(
                getString(R.string.settings_dark_mode_key),
                getResources().getBoolean(R.bool.settings_dark_mode_default)));
    }

    public void setNightMode(Boolean nightValue) {
        if (nightValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController,appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}