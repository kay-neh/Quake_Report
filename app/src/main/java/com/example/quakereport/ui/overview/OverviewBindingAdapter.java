package com.example.quakereport.ui.overview;

import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.example.quakereport.Utils;

import java.util.Date;

public class OverviewBindingAdapter {

    @androidx.databinding.BindingAdapter("bindMagnitude")
    public static void bindMagnitude(TextView textView, OverviewUIState overviewUIState) {
        if(overviewUIState != null){
            //Magnitude and circle background
            textView.setText(Utils.formatMagnitude(overviewUIState.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = Utils.getMagnitudeColor(textView.getContext(), overviewUIState.getMagnitude());
            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);
        }
    }

    @androidx.databinding.BindingAdapter("bindLocationOffset")
    public static void bindLocationOffset(TextView textView, OverviewUIState overviewUIState){
        if(overviewUIState != null){
            //Location
            String locale = overviewUIState.getPlace();
            if(locale != null){
                if (locale.contains("of")) {
                    String[] location = locale.split("(?<=of )");
                    textView.setText(location[0].toUpperCase());
                } else {
                    String offset = "NEAR THE";
                    textView.setText(offset);
                }
            }
        }
    }

    @androidx.databinding.BindingAdapter("bindPrimaryLocation")
    public static void bindPrimaryLocation(TextView textView, OverviewUIState overviewUIState){
        if(overviewUIState != null){
            //Location
            String locale = overviewUIState.getPlace();
            if(locale != null){
                if (locale.contains("of")) {
                    String[] location = locale.split("(?<=of )");
                    textView.setText(location[1]);
                } else {
                    textView.setText(locale);
                }
            }
        }
    }

    @androidx.databinding.BindingAdapter("bindDate")
    public static void bindDate(TextView textView, OverviewUIState overviewUIState){
        if(overviewUIState != null){
            Date dateObject = new Date(overviewUIState.getTime());
            textView.setText(Utils.formatDate(dateObject));
        }
    }

    @androidx.databinding.BindingAdapter("bindTime")
    public static void bindTime(TextView textView, OverviewUIState overviewUIState){
        if(overviewUIState != null){
            Date dateObject = new Date(overviewUIState.getTime());
            textView.setText(Utils.formatTime(dateObject));
        }
    }

}