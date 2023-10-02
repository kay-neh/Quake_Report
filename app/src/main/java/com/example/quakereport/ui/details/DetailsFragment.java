package com.example.quakereport.ui.details;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.quakereport.R;
import com.example.quakereport.databinding.FragmentDetailsBinding;
import com.example.quakereport.databinding.FragmentDetailsNewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsFragment extends Fragment {

    FragmentDetailsNewBinding binding;
    GoogleMap map;
    LatLng location;
    String webpage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details_new,container,false);

        String[] argument = DetailsFragmentArgs.fromBundle(getArguments()).getEventId();
        setTitle(argument[1]);

        //Inflating menus
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.details_options_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if(itemId == R.id.browser){
                    openWebPage(webpage);
                    Log.e("URL", webpage);
                    return true;
                }else if(itemId == R.id.normal_map){
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    return true;
                }else if(itemId == R.id.hybrid_map){
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;
                }else if(itemId == R.id.satellite_map){
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;
                }else if(itemId == R.id.terrain_map){
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;
                }
                return false;
            }

        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        Application application = this.getActivity().getApplication();

        DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory(application,argument[0]);
        DetailsViewModel detailsViewModel = new ViewModelProvider(this, detailsViewModelFactory).get(DetailsViewModel.class);

        binding.setDetailsViewModel(detailsViewModel);
        binding.setLifecycleOwner(this);

        detailsViewModel.detailsUIState.observe(this, new Observer<DetailsUIState>() {
            @Override
            public void onChanged(DetailsUIState detailsUIState) {
                webpage = detailsUIState.getUrl();
                location = new LatLng(detailsUIState.getLatitude(),detailsUIState.getLongitude());
                setMarker();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
            }
        });


        return binding.getRoot();
    }

    public void setMarker(){
        float zoomLevel = 5f;
        // Adding overlay to map
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(location)
                .radius(1000.0); // In meters

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.addMarker(new MarkerOptions().position(location).title("Earthquake"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));
        map.addCircle(circleOptions);
    }

    public void setTitle(String title){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try{
            startActivity(intent);
        }catch (Exception e){
            Log.e("Error opening intent", e.getMessage());
        }
    }

}