package com.example.quakeviewer;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
        this.mostNorthQuake = null;
        this.mostSouthQuake = null;
        this.mostWestQuake = null;
        this.mostEastQuake = null;
        this.largestQuake = null;
        this.shallowestQuake = null;
        this.Refresh();
    }

    public void Refresh() {
        QuakeTask quakeTask = new QuakeTask();
        quakeTask.execute(this.dataSrc);
    }

    private void AddQuake(QuakeItem item) {
        this.quakes.add(item);

        if (this.quakes.size() == 1) {
            this.mostNorthQuake = item;
            this.mostSouthQuake = item;
            this.mostWestQuake = item;
            this.mostEastQuake = item;
            this.largestQuake = item;
            this.shallowestQuake = item;
            return;
        }

        if (item.IsMoreNorth(this.mostNorthQuake))
            this.mostNorthQuake = item;
        if (!item.IsMoreNorth(this.mostSouthQuake))
            this.mostSouthQuake = item;
        if (item.IsMoreEast(this.mostEastQuake))
            this.mostEastQuake = item;
        if (!item.IsMoreEast(this.mostWestQuake))
            this.mostWestQuake = item;
        if (item.IsDeeper(this.largestQuake))
            this.largestQuake = item;
        if (!item.IsDeeper(this.shallowestQuake))
            this.shallowestQuake = item;
    }

    private void ClearQuakes() {
        this.quakes.clear();
        this.mostNorthQuake = null;
        this.mostSouthQuake = null;
        this.mostWestQuake = null;
        this.mostEastQuake = null;
        this.largestQuake = null;
        this.shallowestQuake = null;
    }
    private Boolean ParseFeed(InputStream feedStream) {
        ClearQuakes();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(feedStream, null);

            Boolean quakeItem = false;
            String quakeData = null;
            String quakeLat = null;
            String quakeLong = null;

            while (xmlPullParser.next() != xmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();
                String name = xmlPullParser.getName();

                if (name == null)
                    continue;


                if (eventType == xmlPullParser.START_TAG && name.equalsIgnoreCase("item")) {
                    quakeItem = true;
                    continue;
                }

                if (eventType == xmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item"))
                        quakeItem = false;
                    continue;
                }

                if (quakeItem) {
                    String value = "";
                    if (xmlPullParser.next() == xmlPullParser.TEXT) {
                        value = xmlPullParser.getText();
                        xmlPullParser.nextTag();
                    }

                    if (name.equalsIgnoreCase("description"))
                        quakeData = value;
                    else if (name.equalsIgnoreCase("lat"))
                        quakeLat = value;
                    else if (name.equalsIgnoreCase("long"))
                        quakeLong = value;

                    if (quakeData != null && quakeLat != null && quakeLong != null) {
                        QuakeItem item = new QuakeItem(quakeData, quakeLat, quakeLong);
                        AddQuake(item);
                    }
                }

            }
        } catch (IOException e) {
            Log.e("e", "Error", e);
        } catch (XmlPullParserException e) {
            Log.e("e", "Error", e);
        }
        return true;
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
                return ParseFeed(feedStream);
            } catch (IOException e) {
                Log.e("e", "Error", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
        }
    }
}