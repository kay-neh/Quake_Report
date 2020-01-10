package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {
    ListView earthquakeListView;

    TextView emptyTxt,emptyTxtDesc;
    ImageView emptyImg;
    LinearLayout emptyState;
    MaterialButton refresh;

    EarthquakeAdapter adapter;
    ProgressBar loading;
    SwipeRefreshLayout swipe;

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
        swipe = findViewById(R.id.swipe_down);

        startBackgroundThread();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                emptyState.setVisibility(View.GONE);
                startBackgroundThread();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyState.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                startBackgroundThread();
            }
        });
    }


    //menu items
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
    }//menu item end


    public void updateUI(ArrayList<Earthquakes> earthquakes) {

        adapter = new EarthquakeAdapter(this, earthquakes);
        earthquakeListView.setAdapter(adapter);
        loading.setVisibility(View.GONE);
        swipe.setRefreshing(false);
        earthquakeListView.setEmptyView(emptyState);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*adapter.clear();
                emptyTxt.setText(R.string.no_quake);
                emptyTxtDesc.setText(R.string.no_quake_desc);
                emptyImg.setImageResource(R.drawable.no_event);
                */
            }
        });
    }

    public void startBackgroundThread(){
        final String USGS_API_BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String limit = sharedPrefs.getString(
                getString(R.string.settings_limit_key),
                getString(R.string.settings_limit_default));

        Uri baseUri = Uri.parse(USGS_API_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        //checks for an active internet connection
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) { networkInfo = connMgr.getActiveNetworkInfo(); }

        // Control flow for no internet connection
        if (networkInfo != null && networkInfo.isConnected()) {
            final EarthQuakeAsyncTask job = new EarthQuakeAsyncTask();
            job.execute(uriBuilder.toString());
        }else{
            loading.setVisibility(View.GONE);
            swipe.setRefreshing(false);
            //test data
            emptyTxt.setText(R.string.no_internet);
            emptyTxtDesc.setText(R.string.no_internet_desc);
            emptyImg.setImageResource(R.drawable.no_network);
            earthquakeListView.setEmptyView(emptyState);
        }
    }


    private class EarthQuakeAsyncTask extends AsyncTask<String,Void,ArrayList<Earthquakes>>{
        @Override
        protected ArrayList<Earthquakes> doInBackground(String... apiEndPoint) {
           return QueryUtils.backgroundWork(apiEndPoint[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquakes> earthquakes) {
         if(earthquakes == null){
             //removes the Progress bar when no data returns from the api
             loading = findViewById(R.id.progress_bar);

             loading.setVisibility(View.GONE);
             //stops the Swipe-refreshing loading
             swipe = findViewById(R.id.swipe_down);
             swipe.setRefreshing(false);
             //sets the Textview for the empty state
             earthquakeListView = findViewById(R.id.list);
             //empty state views
             emptyState = findViewById(R.id.new_empty_test);
             emptyImg = findViewById(R.id.empty_img);
             emptyTxt = findViewById(R.id.empty_txt);
             emptyTxtDesc = findViewById(R.id.empty_txt_desc);

             emptyTxt.setText(R.string.no_quake);
             emptyTxtDesc.setText(R.string.no_quake_desc);
             emptyImg.setImageResource(R.drawable.no_event);

             earthquakeListView.setEmptyView(emptyState);
             return;
            }
            updateUI(earthquakes);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
    }

    public void darkMode(){

    }

}
