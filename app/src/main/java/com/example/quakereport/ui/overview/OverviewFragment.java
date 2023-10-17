package com.example.quakereport.ui.overview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
    OverviewViewModel overviewViewModel;
    Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_overview,container,false);

        activity = getActivity();

        //Inflating menus
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.settings_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
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

        overviewViewModel.overviewUIStateList.observe(getViewLifecycleOwner(), overviewUIStateList -> {
            if (overviewUIStateList != null) {
                Log.e("StateList size", String.valueOf(overviewUIStateList.size()));
                adapter.submitList(overviewUIStateList);
                if(!overviewUIStateList.isEmpty()){
                    overviewViewModel.onProgressBarTriggered();
                }
            }
        });

        overviewViewModel.progressBarEvent.observe(this, aBoolean -> {
            if(aBoolean){
                binding.progressBar.setVisibility(View.VISIBLE);
            }else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        overviewViewModel.snackBarEvent.observe(this, aBoolean -> {
            if(aBoolean){
                Snackbar.make(binding.swipeDown, "Fetching the latest updates...", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getContext().getColor(R.color.snackBarColor))
                        .setTextColor(getContext().getColor(R.color.white))
                        .show();
                overviewViewModel.onSnackBarTriggered();
            }
        });

        overviewViewModel.navigateToEarthquakeDetails.observe(this, data -> {
            if(data != null){
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(OverviewFragmentDirections.actionOverviewFragmentToDetailsFragment(data));
                overviewViewModel.onEarthquakeDetailsNavigated();
            }

        });

        onSharedPreferenceChangeListener = (sharedPreferences, key) -> {
                if (key.equals(getContext().getString(R.string.settings_order_by_key))) {
                    overviewViewModel.getOverViewUIStateList(false, sharedPreferences.getString(key, getContext().getString(R.string.settings_order_by_default))
                            ,
                            sharedPreferences.getString(
                                    getContext().getString(R.string.settings_limit_key),
                                    getContext().getString(R.string.settings_limit_default)));
                }
                if (key.equals(getContext().getString(R.string.settings_limit_key))) {
                    overviewViewModel.getOverViewUIStateList(false, sharedPreferences.getString(getContext().getString(R.string.settings_order_by_key),
                                    getContext().getString(R.string.settings_order_by_default))
                            ,
                            sharedPreferences.getString(key, getContext().getString(R.string.settings_limit_default)));
                }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

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
            overviewViewModel.triggerSnackBar();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sharedPrefs != null){
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

}