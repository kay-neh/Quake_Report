package com.example.quakereport.ui.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailsViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String id;

    public DetailsViewModelFactory(@NonNull Application application, String id) {
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return (T) new DetailsViewModel(id, application);
        }
        throw new IllegalArgumentException("Unable to construct Viewmodel");
    }


}
