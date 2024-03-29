package com.example.quakereport.ui.details;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.quakereport.R;
import com.example.quakereport.databinding.FragmentDetailsBottomsheetBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

public class DetailsFragment extends Fragment {

    int REQUEST_LOCATION_PERMISSION = 1;
    int REQUEST_TURN_DEVICE_LOCATION_ON = 2;
    FragmentDetailsBottomsheetBinding binding;
    GoogleMap map;
    LatLng location;
    String webpage;
    Location myLocation;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details_bottomsheet,container,false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

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

        detailsViewModel.detailsUIState.observe(this, detailsUIState -> {
            webpage = detailsUIState.getUrl();
            location = new LatLng(detailsUIState.getLatitude(),detailsUIState.getLongitude());
            setMarker();
            checkPermissionAndGetLocation();
        });

        return binding.getRoot();
    }

    public void checkPermissionAndGetLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkDeviceLocationSettingAndGetCurrentLocation(true);
        } else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    public void checkDeviceLocationSettingAndGetCurrentLocation(boolean resolve){
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_LOW_POWER,1000).build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());
        locationSettingsResponseTask.addOnFailureListener(exception -> {
            if(exception instanceof ResolvableApiException && resolve){
                try {
                    startIntentSenderForResult(((ResolvableApiException) exception).getResolution().getIntentSender(), REQUEST_TURN_DEVICE_LOCATION_ON, null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException ex) {
                    Log.e("Location Setting exception", "Error getting location settings resolution: " + ex.getMessage());
                }
            } else {
                Snackbar snackbar = Snackbar.make(binding.snackbarCoordinate, "Location services must be enabled", Snackbar.LENGTH_INDEFINITE)
                        .setAnchorView(binding.snackbarConstraint)
                        .setBackgroundTint(getContext().getColor(R.color.snackBarColor))
                        .setTextColor(getContext().getColor(R.color.white))
                        .setAction("TURN ON", v -> checkDeviceLocationSettingAndGetCurrentLocation(true))
                        .setActionTextColor(getContext().getColor(R.color.colorSecondary));
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(binding.snackbarConstraint);
                snackbar.setBehavior(new SnackbarBehavior(bottomSheetBehavior));
                snackbar.show();

            }
        });

        locationSettingsResponseTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                // do the thing
                getCurrentLocation();
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        // Check if location permission was granted first
        // before enabling my location else request permission first
        // also get user's current location and use it to calculate distance to marked point
        map.setMyLocationEnabled(true);
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return new CancellationTokenSource().getToken();
            }
            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            if(location != null){
                myLocation = location;
                Log.e("Dev Location", "Lat: 4.8803502, Lng: 7.0417099");
                Log.e("Device Location", "Lat: " +myLocation.getLatitude() +", Lng: " +myLocation.getLongitude());
                calculateAndDisplayDistance(myLocation);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            if(resultCode == Activity.RESULT_OK){
                checkDeviceLocationSettingAndGetCurrentLocation(true);
            }else{
                checkDeviceLocationSettingAndGetCurrentLocation(false);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length >= 1 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // check if device location is on
                checkDeviceLocationSettingAndGetCurrentLocation(true);
            }
        }
    }

    public void setMarker(){
        float zoomLevel = 5f;
        // Adding overlay to map
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(location)
                .radius(1000.0); // In meters

        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.addMarker(new MarkerOptions().position(location).title("Earthquake"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));
        map.addCircle(circleOptions);
    }

    public void calculateAndDisplayDistance(Location myLocation){
        Location markerLocation = new Location("");
        markerLocation.setLatitude(location.latitude);
        markerLocation.setLongitude(location.longitude);
        float result = myLocation.distanceTo(markerLocation);
        String distance = String.valueOf(result/1000);
        binding.distance.setText(distance + " km away");
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