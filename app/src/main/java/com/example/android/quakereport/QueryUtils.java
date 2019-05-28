package com.example.android.quakereport;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
     * Helper methods related to requesting and receiving earthquake data from USGS.
     */
    public final class QueryUtils {

        private QueryUtils() {
        }

        public static ArrayList<Quake> fetchEarthquakeData(String requestUrl) {
            // Create URL object
            URL url = createUrl(requestUrl);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing input stream", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            ArrayList<Quake> earthquakes = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event}
            return earthquakes;
        }

        private static URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL ", e);
            }
            return url;
        }

        private static String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private static String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private static ArrayList<Quake> extractFeatureFromJson(String earthquakeJSON) {
            // If the JSON string is empty or null, then return early.

            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }

            ArrayList<Quake> earthquakes = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                // If there are results in the features array
                for (int i = 0; i < featureArray.length(); i++) {

                    JSONObject firstFeature = featureArray.getJSONObject(i);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    // Extract out the title, number of people, and perceived strength values
                    double magnitude = properties.getDouble("mag");
                    String place = properties.getString("place");
                    long time_ms = properties.getLong("time");
                    String url = properties.getString("url");

                    earthquakes.add(new Quake(magnitude,place,time_ms,url));

                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return earthquakes;
        }
    }
