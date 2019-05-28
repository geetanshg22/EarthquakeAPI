package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Quake>> {

    private String mUrl;

    public EarthquakeLoader(Context context, String URL) {
        super(context);
        mUrl = URL;
    }
    @Override
    public ArrayList<Quake> loadInBackground() {

        if(mUrl == null) {
            return null;
        }
        return QueryUtils.fetchEarthquakeData(mUrl);

    }

}
