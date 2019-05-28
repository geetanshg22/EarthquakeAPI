package com.example.android.quakereport;

class Quake {

    private double mEarthquake;

    private String mPlace;

    private long mDate;

    private String mUrl;

    public Quake(double earthquake, String place, long date, String url){
        mEarthquake = earthquake;
        mPlace = place;
        mDate = date;
        mUrl = url;
    }

    public double getEarthquake(){
        return mEarthquake;
    }

    public String getPlace(){
        return mPlace;
    }

    public long getDate(){
        return mDate;
    }

    public String getUrl(){
        return mUrl;
    }
}
