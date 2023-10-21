package com.example.quakereport.ui.details;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.BaseTransientBottomBar;

public class SnackbarBehavior extends BaseTransientBottomBar.Behavior {

    private final BottomSheetBehavior bottomSheetBehavior;

    public SnackbarBehavior(BottomSheetBehavior bottomSheetBehavior) {
        super();
        this.bottomSheetBehavior = bottomSheetBehavior;

    }

    @Override
    public boolean canSwipeDismissView(View child) {
        return false;
    }

}
