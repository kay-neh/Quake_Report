package com.example.quakereport.ui.overview;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.quakereport.R;
import com.example.quakereport.databinding.FragmentOverviewBinding;
import com.google.android.material.snackbar.Snackbar;

public class OverviewFragment extends Fragment {

    FragmentOverviewBinding binding;
    OverviewAdapter adapter;
    SharedPreferences sharedPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener spListen;
    OverviewViewModel overviewViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_overview,container,false);

        //Inflating menus
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.settings_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                return NavigationUI.onNavDestinationSelected(menuItem, navController);
            }

        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        //Init viewModel
        overviewViewModel = new ViewModelProvider(this).get(OverviewViewModel.class);

        //Initialize all ui components
        initAdapter();

        //Set swipe down action
        swipeDownAction();

        //Init sharedPreference, get list and register listener
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        setNightModeWithPreference();
        overviewViewModel.overviewUIStateList.observe(getViewLifecycleOwner(), overviewUIStateList -> {
            if (overviewUIStateList != null) {
                Log.i("StateList size", String.valueOf(overviewUIStateList.size()));
                adapter.submitList(overviewUIStateList);
                if(!overviewUIStateList.isEmpty()){
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
        setPreferenceListener();
        sharedPrefs.registerOnSharedPreferenceChangeListener(spListen);

        overviewViewModel.navigateToEarthquakeDetails.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] data) {
                if(data != null){
                    NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(OverviewFragmentDirections.actionOverviewFragmentToDetailsFragment(data));
                    overviewViewModel.onEarthquakeDetailsNavigated();
                }

            }
        });

        return binding.getRoot();
    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        binding.list.setLayoutManager(llm);
        adapter = new OverviewAdapter((eventId,location) -> {
            //handle click events here
            String[] data = {eventId,location};
            overviewViewModel.onEarthquakeClicked(data);
        });
        binding.list.setAdapter(adapter);
    }

    public void swipeDownAction(){
        binding.swipeDown.setOnRefreshListener(() -> {
            overviewViewModel.refreshDataSource();
            binding.swipeDown.setRefreshing(false);
            Snackbar.make(binding.swipeDown, "Fetching the latest updates...", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getContext().getColor(R.color.snackBarColor))
                    .setTextColor(getContext().getColor(R.color.colorOnSurface))
                    .show();
        });
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

    public void setPreferenceListener(){
        //Set up OnSharedPrefChange listener object
        spListen = (sharedPreferences, key) -> {
            if (key.equals(getContext().getString(R.string.settings_order_by_key))) {
                overviewViewModel.getOverViewUIStateList(sharedPreferences.getString(key, getContext().getString(R.string.settings_order_by_default))
                        ,
                        sharedPreferences.getString(
                                getContext().getString(R.string.settings_limit_key),
                                getContext().getString(R.string.settings_limit_default)));
            }
            if (key.equals(getString(R.string.settings_limit_key))) {
                overviewViewModel.getOverViewUIStateList(sharedPreferences.getString(getContext().getString(R.string.settings_order_by_key),
                                getContext().getString(R.string.settings_order_by_default))
                        ,
                        sharedPreferences.getString(key, getContext().getString(R.string.settings_limit_default)));
            }
            if (key.equals(getContext().getString(R.string.settings_dark_mode_key))) {
                setNightMode(sharedPreferences.getBoolean(key, getContext().getResources().getBoolean(R.bool.settings_dark_mode_default)));
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(spListen);
    }
}