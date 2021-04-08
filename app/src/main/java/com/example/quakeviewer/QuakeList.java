package com.example.quakeviewer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class QuakeList {
    private URL dataSrc;
    private AppCompatActivity uiActivity;
    private DatePickerDialog datePicker;
    private List<QuakeItem> quakes;
    private QuakeItem mostNorthQuake;
    private QuakeItem mostSouthQuake;
    private QuakeItem mostWestQuake;
    private QuakeItem mostEastQuake;
    private QuakeItem largestQuake;
    private QuakeItem deepestQuake;
    private QuakeItem shallowestQuake;
    private QuakeItem oldestQuake;
    private QuakeItem newestQuake;

    public List<QuakeItem> getQuakes() { return this.quakes; }
    public QuakeItem getMostNorthQuake() { return this.mostNorthQuake; }
    public QuakeItem getMostSouthQuake() { return this.mostSouthQuake; }
    public QuakeItem getMostWestQuake() { return this.mostWestQuake; }
    public QuakeItem getMostEastQuake() { return this.mostEastQuake; }
    public QuakeItem getLargestQuake() { return this.largestQuake; }
    public QuakeItem getDeepestQuake() { return this.deepestQuake; }
    public QuakeItem getShallowestQuake() { return this.shallowestQuake; }
    public QuakeItem getOldestQuake() { return this.oldestQuake; }
    public QuakeItem getNewestQuake() { return this.newestQuake; }

    public QuakeList(URL url, AppCompatActivity uiActivity, DatePickerDialog datePicker) {
        this.dataSrc = url;
        this.uiActivity = uiActivity;
        this.datePicker = datePicker;
        this.quakes = new ArrayList<QuakeItem>();
        this.mostNorthQuake = null;
        this.mostSouthQuake = null;
        this.mostWestQuake = null;
        this.mostEastQuake = null;
        this.largestQuake = null;
        this.deepestQuake = null;
        this.shallowestQuake = null;
        this.oldestQuake = null;
        this.newestQuake = null;
        this.Refresh();
    }

    public void Refresh() {
        Log.e("INFO", "STARTING REFRESH");
        QuakeTask quakeTask = new QuakeTask();
        quakeTask.execute(this.dataSrc);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void OpenDateRange(Date fromDate, Date toDate) {
        QuakeItem mostNorthQuake = null;
        QuakeItem mostSouthQuake = null;
        QuakeItem mostWestQuake = null;
        QuakeItem mostEastQuake = null;
        QuakeItem largestQuake = null;
        QuakeItem deepestQuake = null;
        QuakeItem shallowestQuake = null;

        List<QuakeItem> filteredQuakes = this.quakes.stream()
                .filter(item -> item.getOrigin().after(fromDate) && item.getOrigin().before(toDate))
                .collect(Collectors.toList());

        int filterSize = filteredQuakes.size();

        if (filterSize == 0)
            return;

        QuakeItem item = filteredQuakes.get(0);
        mostNorthQuake = item;
        mostSouthQuake = item;
        mostWestQuake = item;
        mostEastQuake = item;
        largestQuake = item;
        deepestQuake = item;
        shallowestQuake = item;

        if (filterSize > 1) {
            for (int i = 1; i < filterSize; i++) {
                item = filteredQuakes.get(i);
                if (item.IsMoreNorth(mostNorthQuake))
                    mostNorthQuake = item;
                if (!item.IsMoreNorth(mostSouthQuake))
                    mostSouthQuake = item;
                if (item.IsMoreEast(mostEastQuake))
                    mostEastQuake = item;
                if (!item.IsMoreEast(mostWestQuake))
                    mostWestQuake = item;
                if (item.IsLarger(largestQuake))
                    largestQuake = item;
                if (item.IsDeeper(deepestQuake))
                    deepestQuake = item;
                if (!item.IsDeeper(shallowestQuake))
                    shallowestQuake = item;
            }
        }

        ArrayList<QuakeItem> quakeData = new ArrayList<>();
        quakeData.add(mostNorthQuake);
        quakeData.add(mostSouthQuake);
        quakeData.add(mostWestQuake);
        quakeData.add(mostEastQuake);
        quakeData.add(largestQuake);
        quakeData.add(deepestQuake);
        quakeData.add(shallowestQuake);

        Intent intent = new Intent(uiActivity.getApplicationContext(), QuakeDateFilter.class);
        intent.putExtra("QuakeData",  quakeData);
        uiActivity.startActivity(intent);
    }

    private void AddQuake(QuakeItem item) {
        this.quakes.add(item);

        if (this.quakes.size() == 1) {
            this.mostNorthQuake = item;
            this.mostSouthQuake = item;
            this.mostWestQuake = item;
            this.mostEastQuake = item;
            this.largestQuake = item;
            this.deepestQuake = item;
            this.shallowestQuake = item;
            this.oldestQuake = item;
            this.newestQuake = item;
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
        if (item.IsLarger(this.largestQuake))
            this.largestQuake = item;
        if (item.IsDeeper(this.deepestQuake))
            this.deepestQuake = item;
        if (!item.IsDeeper(this.shallowestQuake))
            this.shallowestQuake = item;
        if (item.IsNewer(this.newestQuake))
            this.newestQuake = item;
        if (!item.IsNewer(this.oldestQuake))
            this.oldestQuake = item;
    }

    private void ClearQuakes() {
        this.quakes.clear();
        this.mostNorthQuake = null;
        this.mostSouthQuake = null;
        this.mostWestQuake = null;
        this.mostEastQuake = null;
        this.largestQuake = null;
        this.shallowestQuake = null;
        this.oldestQuake = null;
        this.newestQuake = null;
    }
    private Boolean ParseFeed(InputStream feedStream) {
        Log.e("INFO", "PARSING FEED");
        ClearQuakes();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(feedStream, null);
            xmlPullParser.nextTag();

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

                        quakeData = null;
                        quakeLat = null;
                        quakeLong = null;
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
        private Calendar dateToCalendar(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }

        @Override
        protected void onPreExecute() {
            Log.e("INFO", "PRE EXECUTE");
            Button uiFilterBtn = (Button) uiActivity.findViewById(R.id.quake_filter_button);
            uiFilterBtn.setClickable(false);
        }

        @Override
        protected Boolean doInBackground(URL... urls) {
            Log.e("INFO", "DO IN BACKGROUND");
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
            Log.e("INFO", "POST EXECUTE");
            RecyclerView.Adapter<QuakeAdapter.ViewHolder> uiRecyclerAdapter = new QuakeAdapter(getQuakes());
            RecyclerView uiRecyclerView = (RecyclerView) uiActivity.findViewById(R.id.quake_list);
            uiRecyclerView.setAdapter(uiRecyclerAdapter);

            datePicker.setMinDate(dateToCalendar(getOldestQuake().getOrigin()));
            datePicker.setMaxDate(dateToCalendar(getNewestQuake().getOrigin()));

            Button uiFilterBtn = (Button) uiActivity.findViewById(R.id.quake_filter_button);
            uiFilterBtn.setClickable(true);
        }
    }
}