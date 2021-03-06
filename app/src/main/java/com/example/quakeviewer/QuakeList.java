package com.example.quakeviewer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

// S1916169 - Fraser Watt (Plagiarism check)
public class QuakeList {
    //vars
    private URL dataSrc;
    private AppCompatActivity uiActivity;
    private DatePickerDialog datePicker;
    private AlertDialog alertDialog;
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
    private Handler refreshHandler;
    private Runnable refreshRunnable;
    private final String infoTag = "QuakeList";

    //getters
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

    public QuakeList(URL url, AppCompatActivity uiActivity, DatePickerDialog datePicker, AlertDialog alertDialog) {
        Log.i(infoTag, "Init");
        this.dataSrc = url;
        this.uiActivity = uiActivity;
        this.datePicker = datePicker;
        this.alertDialog = alertDialog;
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

        this.refreshHandler = new Handler();
        this.refreshRunnable = new Runnable() {
            @Override
            public void run() {
                Log.i(infoTag, "Auto-refresh");
                Refresh();
                refreshHandler.postDelayed(this, 900000);
            }
        };

        Log.i(infoTag, "Init auto refresh task");
        this.refreshHandler.postDelayed(refreshRunnable, 900000);
    }

    //Refresh the list of earthquakes
    //Pulls and parses the latest RSS Feed
    public void Refresh() {
        Log.i(infoTag, "Begin list refresh");
        QuakeTask quakeTask = new QuakeTask();
        quakeTask.execute(this.dataSrc);
    }

    //Filter list of earthquakes to a specific date or date range
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void OpenDateRange(Date fromDate, Date toDate) {
        QuakeItem mostNorthQuake = null;
        QuakeItem mostSouthQuake = null;
        QuakeItem mostWestQuake = null;
        QuakeItem mostEastQuake = null;
        QuakeItem largestQuake = null;
        QuakeItem deepestQuake = null;
        QuakeItem shallowestQuake = null;

        Log.i(infoTag, "Filter list items (fromDate/toDate)");
        List<QuakeItem> filteredQuakes = this.quakes.stream()
                .filter(item -> item.getOrigin().compareTo(fromDate) >= 0 && item.getOrigin().compareTo(toDate) <= 0)
                .collect(Collectors.toList());

        int filterSize = filteredQuakes.size();
        int mainSize = this.quakes.size();

        Log.i(infoTag, "Check if filtered list is empty");
        if (filterSize == 0) {
            Log.i(infoTag, "Show empty list warning");
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("No earthquakes found within the date range specified!");
            alertDialog.show();
            return;
        }

        //Skips calcs if the filter data contains the same amount as the original
        Log.i(infoTag, "Check if filtered list matches the main list");
        if (filterSize == mainSize) {
            mostNorthQuake = this.mostNorthQuake;
            mostSouthQuake = this.mostSouthQuake;
            mostWestQuake = this.mostWestQuake;
            mostEastQuake = this.mostEastQuake;
            largestQuake = this.largestQuake;
            deepestQuake = this.deepestQuake;
            shallowestQuake = this.shallowestQuake;
        } else {
            Log.i(infoTag, "Find filtered quake stats (north/south/west/east/largest/deepest/shallowest)");
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
        }

        Log.i(infoTag, "Pack filtered statistical data");
        ArrayList<QuakeItem> quakeData = new ArrayList<>();
        quakeData.add(mostNorthQuake);
        quakeData.add(mostSouthQuake);
        quakeData.add(mostWestQuake);
        quakeData.add(mostEastQuake);
        quakeData.add(largestQuake);
        quakeData.add(deepestQuake);
        quakeData.add(shallowestQuake);

        Log.i(infoTag, "Open QuakeDateFilter view");
        Intent intent = new Intent(uiActivity.getApplicationContext(), QuakeDateFilter.class);
        intent.putExtra("QuakeData",  quakeData);
        uiActivity.startActivity(intent);
    }

    //Add earthquake to the list
    private void AddQuake(QuakeItem item) {
        Log.i(infoTag, "Add item to Quake");
        this.quakes.add(item);

        if (this.quakes.size() == 1) {
            Log.i(infoTag, "Init statistical data");
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

        Log.i(infoTag, "Compare statistical data");
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

    //Clear list items
    private void ClearQuakes() {
        Log.i(infoTag, "Clear list");
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

    //Parse XML input to QuakeItem
    private Boolean ParseFeed(InputStream feedStream) {
        Log.i(infoTag, "Start feed parse");
        ClearQuakes();

        try {
            Log.i(infoTag, "Init XML Parser");
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(feedStream, null);
            xmlPullParser.nextTag();

            Log.i(infoTag, "Init temp parse vars");
            Boolean quakeItem = false;
            String quakeData = null;
            String quakeLat = null;
            String quakeLong = null;

            Log.i(infoTag, "Loop through XML data");
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
            return false;
        } catch (XmlPullParserException e) {
            Log.e("e", "Error", e);
            return false;
        } finally {
            //Close the stream after XML is parsed
            try {
                Log.i(infoTag, "Close XML data stream");
                feedStream.close();
            } catch (IOException e) {
                Log.e("e", "Error", e);
            }
        }

        return true;
    }

    //Async background task used for pulling data from the RSS Feed and updating the UI
    private class QuakeTask extends AsyncTask<URL, Integer, Boolean> {
        private Calendar dateToCalendar(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }

        @Override
        protected void onPreExecute() {
            Log.i(infoTag, "Setup UI for refresh Task");
            Log.i(infoTag, "Disable filter button");
            Button uiFilterBtn = (Button) uiActivity.findViewById(R.id.quake_filter_button);
            uiFilterBtn.setClickable(false);

            Log.i(infoTag, "Show refresh spinner");
            SwipeRefreshLayout uiRefreshLayout = uiActivity.findViewById(R.id.quake_swipe_container);
            uiRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(URL... urls) {
            Log.i(infoTag, "Start refresh");
            if (urls.length == 0)
                return false;

            try {
                URL feed = urls[0];
                Log.i(infoTag, "Open connection");
                URLConnection feedConnection = feed.openConnection();
                Log.i(infoTag, "Fetch input stream");
                InputStream feedStream = feedConnection.getInputStream();
                return ParseFeed(feedStream);
            } catch (IOException e) {
                Log.e("e", "Error", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.i(infoTag, "On refresh completion");
            RecyclerView.Adapter<QuakeAdapter.ViewHolder> uiRecyclerAdapter = new QuakeAdapter(getQuakes());
            RecyclerView uiRecyclerView = (RecyclerView) uiActivity.findViewById(R.id.quake_list);
            Log.i(infoTag, "Set quake items in ui view");
            uiRecyclerView.setAdapter(uiRecyclerAdapter);

            Log.i(infoTag, "Restrict date picker (min/max date)");
            datePicker.setMinDate(dateToCalendar(getOldestQuake().getOrigin()));
            datePicker.setMaxDate(dateToCalendar(getNewestQuake().getOrigin()));

            Log.i(infoTag, "Enable filter button");
            Button uiFilterBtn = (Button) uiActivity.findViewById(R.id.quake_filter_button);
            uiFilterBtn.setClickable(true);

            Log.i(infoTag, "Remove refresh spinner");
            SwipeRefreshLayout uiRefreshLayout = uiActivity.findViewById(R.id.quake_swipe_container);
            uiRefreshLayout.setRefreshing(false);
        }
    }
}