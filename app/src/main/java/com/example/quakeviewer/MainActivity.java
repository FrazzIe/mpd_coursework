package com.example.quakeviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView uiRecyclerView = (RecyclerView) findViewById(R.id.quake_list);
        RecyclerView.LayoutManager uiRecyclerLayout = new GridLayoutManager(this, 1);
        uiRecyclerView.setLayoutManager(uiRecyclerLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("INFO", "STARTED");
        URL feed = null;

        try {
            feed = new URL("https://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(null,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        QuakeList quakeList = new QuakeList(feed, this, datePickerDialog);


        DatePickerDialog.OnDateSetListener datePickerDialogCallback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");
                    Date fromDate = dateFormat.parse(dayOfMonth + "-" + monthOfYear + "-" + year);
                    Date toDate = dateFormat.parse(dayOfMonthEnd + "-" + monthOfYearEnd + "-" + yearEnd);
                    quakeList.OpenDateRange(fromDate, toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        datePickerDialog.setOnDateSetListener(datePickerDialogCallback);

        Button uiFilterBtn = (Button) findViewById(R.id.quake_filter_button);
        uiFilterBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(view.getContext(), QuakeDateFilter.class);
//            view.getContext().startActivity(intent);
            datePickerDialog.show(getFragmentManager(), "datePickerDialog");
        });
    }
}