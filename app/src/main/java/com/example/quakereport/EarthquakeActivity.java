package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quakereport.POJO.Earthquakes;
import com.example.quakereport.Network.GetEarthquakes;
import com.example.quakereport.Network.RetrofitClient;
import com.example.quakereport.POJO.Features;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeActivity extends AppCompatActivity {
    ListView earthquakeListView;

    TextView emptyTxt,emptyTxtDesc;
    ImageView emptyImg;
    View emptyState;
    MaterialButton refresh;

    EarthquakeAdapter adapter;
    ProgressBar loading;

    SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        //shared preference object initialized
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Initialize all ui components
        earthquakeListView = findViewById(R.id.list);

        emptyState = findViewById(R.id.new_empty_test);
        emptyImg = findViewById(R.id.empty_img);
        emptyTxt = findViewById(R.id.empty_txt);
        emptyTxtDesc = findViewById(R.id.empty_txt_desc);
        refresh = findViewById(R.id.refresh_button);
        loading = findViewById(R.id.progress_bar);

        runBackgroundTask();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyState.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                runBackgroundTask();
            }
        });

    }

    //Helper method to make retrofit call to usgs server
    public void runBackgroundTask(){
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String limit = sharedPrefs.getString(
                getString(R.string.settings_limit_key),
                getString(R.string.settings_limit_default));

        GetEarthquakes service = RetrofitClient.getRetrofitInstance().create(GetEarthquakes.class);
        Call<Earthquakes> call = service.getAllEarthquakes("geojson",limit,minMagnitude,orderBy);

        call.enqueue(new Callback<Earthquakes>() {
            @Override
            public void onResponse(Call<Earthquakes> call, Response<Earthquakes> response) {
                if(response.body() != null)
                    updateUI(response.body().getFeatures());
            }

            @Override
            public void onFailure(Call<Earthquakes> call, Throwable t) {
                loading.setVisibility(View.GONE);
                earthquakeListView.setEmptyView(emptyState);
                Toast.makeText(EarthquakeActivity.this,"unable to load users",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUI(List<Features> features) {

        adapter = new EarthquakeAdapter(this, features);
        earthquakeListView.setAdapter(adapter);
        loading.setVisibility(View.GONE);
        //earthquakeListView.setEmptyView(emptyState);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //handle click events
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        runBackgroundTask();
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
}
