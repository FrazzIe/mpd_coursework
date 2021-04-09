package com.example.quakeviewer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// S1916169 - Fraser Watt (Plagiarism check)
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AlertDialog alertDialog;
    private String quakeFeedUrl = "https://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup RecyclerView layout
        RecyclerView uiRecyclerView = (RecyclerView) findViewById(R.id.quake_list);
        RecyclerView.LayoutManager uiRecyclerLayout = new GridLayoutManager(this, 1);
        uiRecyclerView.setLayoutManager(uiRecyclerLayout);

        //Set toolbar as action bar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Log.d("INFO", "STARTED");

        try {
            //Setup feed URL
            URL feed = new URL(quakeFeedUrl);

            //Setup date picker
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(null,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            this.alertDialog = createAlertDialog("", "");

            //Initialise Dynamic Earthquake List
            QuakeList quakeList = new QuakeList(feed, this, datePickerDialog, this.alertDialog);

            //Setup date picker callback after quakeList is defined
            //Filter Earthquakes on callback
            DatePickerDialog.OnDateSetListener datePickerDialogCallback = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy HH:mm:ss");
                        Date fromDate = dateFormat.parse(dayOfMonth + "-" + (++monthOfYear) + "-" + year + " 00:00:00");
                        Date toDate = dateFormat.parse(dayOfMonthEnd + "-" + (++monthOfYearEnd) + "-" + yearEnd+ " 23:59:59");
                        quakeList.OpenDateRange(fromDate, toDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };

            datePickerDialog.setOnDateSetListener(datePickerDialogCallback);

            //Add click event listener to open the date picker
            Button uiFilterBtn = (Button) findViewById(R.id.quake_filter_button);
            uiFilterBtn.setOnClickListener(view -> {
                datePickerDialog.show(getFragmentManager(), "datePickerDialog");
            });

            //Add refresh listener to refresh earthquake list on swipe up
            SwipeRefreshLayout uiRefreshLayout = findViewById(R.id.quake_swipe_container);
            uiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    quakeList.Refresh();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // https://stackoverflow.com/a/36747438
    private AlertDialog createAlertDialog(String msg, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle(title)
                .setNeutralButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CONFIRM
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}