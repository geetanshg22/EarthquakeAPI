/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Quake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    NetworkInfo net;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        getLoaderManager().initLoader(0,null,this).forceLoad();

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        net = connMgr.getActiveNetworkInfo();

    }

    public void UpdateUI(ArrayList<Quake> earthquakes){

        final QuakeAdapter adapter = new QuakeAdapter<>(this, earthquakes);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quake currentEarthquake = (Quake) adapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentEarthquake.getUrl()));
                startActivity(intent);
            }
        });
    }

    ProgressBar pb;

    @Override
    public Loader<ArrayList<Quake>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(getString(R.string.min_magnitude_key),getString(R.string.min_magnitude_default));
        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        pb = (ProgressBar)findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Quake>> loader, ArrayList<Quake> data) {
        if(data!=null && !data.isEmpty()){
            pb.setVisibility(View.GONE);
            UpdateUI(data);
        }
        else if(net == null){
            pb.setVisibility(View.GONE);
            TextView noData = (TextView)findViewById(R.id.nolist);
            noData.setText("No internet Connection");
            noData.setVisibility(View.VISIBLE);
        }
        else{
            pb.setVisibility(View.GONE);
            TextView noData = (TextView)findViewById(R.id.nolist);
            noData.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Quake>> loader) {
        UpdateUI(new ArrayList<Quake>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_setting){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
