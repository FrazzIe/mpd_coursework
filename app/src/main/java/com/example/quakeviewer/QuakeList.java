package com.example.quakeviewer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class QuakeList {
    private URL dataSrc;
    private List<QuakeItem> quakes;
    private QuakeItem mostNorthQuake;
    private QuakeItem mostSouthQuake;
    private QuakeItem mostWestQuake;
    private QuakeItem mostEastQuake;
    private QuakeItem largestQuake;
    private QuakeItem deepestQuake;
    private QuakeItem shallowestQuake;

    public List<QuakeItem> getQuakes() { return this.quakes; }
    public QuakeItem getMostNorthQuake() { return this.mostNorthQuake; }
    public QuakeItem getMostSouthQuake() { return this.mostSouthQuake; }
    public QuakeItem getMostWestQuake() { return this.mostWestQuake; }
    public QuakeItem getMostEastQuake() { return this.mostEastQuake; }
    public QuakeItem getDeepestQuake() { return this.deepestQuake; }
    public QuakeItem getShallowestQuake() { return this.shallowestQuake; }

    public QuakeList(URL url) {
        this.dataSrc = url;
        this.quakes = new ArrayList<QuakeItem>();
        this.Refresh();
    }

    public void Refresh() {
        QuakeTask quakeTask = new QuakeTask();
        quakeTask.execute(this.dataSrc);
    }

    private class QuakeTask extends AsyncTask<URL, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(URL... urls) {
            if (urls.length == 0)
                return false;

            try {
                URL feed = urls[0];
                URLConnection feedConnection = feed.openConnection();
                InputStream feedStream = feedConnection.getInputStream();
                
            } catch (IOException e) {
                Log.e("e", "Error", e);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
        }
    }
}