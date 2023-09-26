package com.example.quakereport.ui.details;

import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.example.quakereport.Utils;

import java.util.Date;

public class DetailsBindingAdapter {

    @androidx.databinding.BindingAdapter("bindDetailsMagnitude")
    public static void bindMagnitude(TextView textView, DetailsUIState detailsUIState) {
        if(detailsUIState != null){
            //Magnitude and circle background
            textView.setText(Utils.formatMagnitude(detailsUIState.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = Utils.getMagnitudeColor(textView.getContext(), detailsUIState.getMagnitude());
            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);
        }
    }

    @androidx.databinding.BindingAdapter("bindDetailsDepth")
    public static void bindDepth(TextView textView, DetailsUIState detailsUIState) {
        if(detailsUIState != null){
            //Magnitude and circle background
            String depth = Utils.formatThreeDecimal(detailsUIState.getDepth()) + " km depth";
            textView.setText(depth);

        }
    }

    @androidx.databinding.BindingAdapter("bindDetailsTime")
    public static void bindDateTime(TextView textView, DetailsUIState detailsUIState){
        if(detailsUIState != null){
            Date dateObject = new Date(detailsUIState.getTime());
            textView.setText(Utils.formatDateTime(dateObject));
        }
    }

    @androidx.databinding.BindingAdapter("bindDetailsCoordinate")
    public static void bindCoordinates(TextView textView, DetailsUIState detailsUIState){
        if(detailsUIState != null){
            textView.setText(Utils.formatPointCoordinates(detailsUIState.getLatitude(), detailsUIState.getLongitude()));
        }
    }


}
