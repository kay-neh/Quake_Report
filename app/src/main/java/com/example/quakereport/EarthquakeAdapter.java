package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quakereport.Database.QuakeData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeVH> {

    public interface ListItemClickListener{
        void onListItemClick(int index);
    }

    final private ListItemClickListener mOnclickListener;
    private Context context;
    private List<QuakeData> quakeData;

    public EarthquakeAdapter(Context context, ListItemClickListener mOnclickListener){
        this.context = context;
        this.mOnclickListener = mOnclickListener;
    }

    class EarthquakeVH extends RecyclerView.ViewHolder {

        TextView magnitude,locationOffset,primaryLocation,date,time;

            EarthquakeVH(@NonNull View itemView) {
            super(itemView);
            magnitude = itemView.findViewById(R.id.magnitude);
            locationOffset = itemView.findViewById(R.id.location_offset);
            primaryLocation = itemView.findViewById(R.id.primary_location);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    mOnclickListener.onListItemClick(index);
                }
            });
        }
    }

    @NonNull
    @Override
    public EarthquakeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.earthquake_recycler_view, parent, false);
        return new EarthquakeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeVH holder, int position) {
        if(quakeData != null){
            QuakeData currentQuake = quakeData.get(position);

            //Date and time
            Date dateObject = new Date(currentQuake.getTime());
            holder.date.setText(formatDate(dateObject));
            holder.time.setText(formatTime(dateObject));

            //Location
            String locale = currentQuake.getPlace();
            if(locale.contains("of")){
                String[] location = locale.split("(?<=of )");
                holder.locationOffset.setText(location[0].toUpperCase());
                holder.primaryLocation.setText(location[1]);
            }else{
                String offset = "NEAR THE";
                holder.locationOffset.setText(offset);
                holder.primaryLocation.setText(locale);
            }

            //Magnitude and circle background
            holder.magnitude.setText(formatMagnitude(currentQuake.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitude.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());
            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);
        }
    }

    @Override
    public int getItemCount() {
        if (quakeData != null)
            return quakeData.size();
        else return 0;
    }

    public void setQuakes(List<QuakeData> quakes){
        quakeData = quakes;
        notifyDataSetChanged();
    }

    public int getRoomItemId(int index){
        return quakeData.get(index).getId();
    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a",Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
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
