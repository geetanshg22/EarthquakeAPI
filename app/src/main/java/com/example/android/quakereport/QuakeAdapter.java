package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

class QuakeAdapter<T> extends ArrayAdapter {
    public QuakeAdapter(EarthquakeActivity earthquakeActivity, ArrayList<Quake> earthquakes) {
        super(earthquakeActivity,0,earthquakes);


    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        final Quake index = (Quake) getItem(position);

        TextView earthquake = (TextView) view.findViewById(R.id.earthquake);
        DecimalFormat decimalFormatting = new DecimalFormat("0.0");
        String formatted = decimalFormatting.format(index.getEarthquake());
        earthquake.setText(formatted);
        int magnitudeColor = (int)Math.floor(index.getEarthquake());
        int magnitude;
        switch (magnitudeColor){
            case 0:
            case 1:
                magnitude = R.color.magnitude1;
                break;
            case 2:
                magnitude = R.color.magnitude2;
                break;
            case 3:
                magnitude = R.color.magnitude3;
                break;
            case 4:
                magnitude = R.color.magnitude4;
                break;
            case 5:
                magnitude = R.color.magnitude5;
                break;
            case 6:
                magnitude = R.color.magnitude6;
                break;
            case 7:
                magnitude = R.color.magnitude7;
                break;
            case 8:
                magnitude = R.color.magnitude8;
                break;
            case 9:
                magnitude = R.color.magnitude9;
                break;
            default:
                magnitude = R.color.magnitude10plus;
                break;
        }
        GradientDrawable gd = (GradientDrawable)earthquake.getBackground();
        gd.setColor(ContextCompat.getColor(getContext(),magnitude));

        TextView placeFirst = (TextView)view.findViewById(R.id.PlaceFirst);
        TextView placeSecond = (TextView)view.findViewById(R.id.PlaceSecond);


        int StringIndex = index.getPlace().indexOf("of");
        if(StringIndex==-1){
            placeSecond.setText(index.getPlace());
            placeFirst.setText("NEAR THE");
        }
        else {
            String FirstHalf = (index.getPlace()).substring(0, StringIndex + 3);
            String SecondHalf = (index.getPlace()).substring(StringIndex + 3);
            placeFirst.setText(FirstHalf.toUpperCase());
            placeSecond.setText(SecondHalf);
        }
        TextView date = (TextView)view.findViewById(R.id.date);
        Date dateObject = new Date(index.getDate());
        SimpleDateFormat dateformatter = new SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = dateformatter.format(dateObject);
        date.setText(formattedDate);

        TextView time = (TextView)view.findViewById(R.id.time);
        Date timeObject = new Date(index.getDate());
        SimpleDateFormat timeformatter = new SimpleDateFormat("h:mm a");
        String formattedtime = timeformatter.format(timeObject);
        time.setText(formattedtime);

        return view;
    }
}
