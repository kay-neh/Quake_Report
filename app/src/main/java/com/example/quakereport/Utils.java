package com.example.quakereport;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getLocation(String place){
        if(place != null) {
            if (place.contains("of")) {
                String[] location = place.split("(?<=of )");
                return location[1];
            } else {
                return place;
            }
        }else {
            return "Unknown Location";
        }
    }

    public static String getDegreeFromLat(double latitude){
        String s = formatThreeDecimal(latitude);
        if(s.contains("-")){
            return s.replace("-", "") + "°S";
        }else {
            return s + "°N";
        }
    }

    public static String getDegreeFromLng(double longitude){
        String s = formatThreeDecimal(longitude);
        if(s.contains("-")){
            return s.replace("-", "") + "°W";
        }else {
            return s + "°E";
        }
    }

    public static String formatPointCoordinates(double latitude, double longitude){
        String lat = getDegreeFromLat(latitude);
        String lng = getDegreeFromLng(longitude);
        return lat + ", " +lng;
    }

    public static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    public static String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    public static String formatDateTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy h:mm a", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    public static String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    public static String formatThreeDecimal(double number){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.000");
        return magnitudeFormat.format(number);
    }

    public static int getMagnitudeColor(Context context, double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }


}
