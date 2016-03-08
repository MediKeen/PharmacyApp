package com.medikeen.pharmacy.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Varun on 3/5/2016.
 */
public class ConnectionDetector extends AsyncTask<Void, Boolean, Boolean> {

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            URL url = new URL("http://www.medikeen.com/android_api/index.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Log.e("STRING ENTITY ERROR: ", "STRING ENTITY ERROR: " + e);
            return false;
        }
    }

    public boolean isInternetConnected() {
        try {

            boolean isInternetConnected = get();

            Log.e("IS INTERNET: ", "IS INTERNET: " + isInternetConnected);

            return isInternetConnected;
        } catch (Exception e) {
            Log.e("IS INTERNET ERROR: ", "IS INTERNET ERROR: " + e);
            e.printStackTrace();
            return false;
        }
    }
}
