package com.example.quakeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuakeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_details);

        Intent intent = getIntent();
        long origin = intent.getLongExtra("origin", 0);
        String location = intent.getStringExtra("location");
        int depth = intent.getIntExtra("depth", 0);
        String depthMeasurement = intent.getStringExtra("depthMeasurement");
        Double latitude = intent.getDoubleExtra("latitude", 0.0);
        Double longitude = intent.getDoubleExtra("longitude", 0.0);
        Double magnitude = intent.getDoubleExtra("magnitude", 0.0);

        TextView dateText = findViewById(R.id.info_date);
        TextView locationText = findViewById(R.id.info_location);
        TextView magnitudeText = findViewById(R.id.info_magnitude);
        TextView depthText = findViewById(R.id.info_depth);

        Date originDate = new Date(origin);
        SimpleDateFormat originFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");


        dateText.setText(originFormat.format(originDate));
        locationText.setText(location);
        magnitudeText.setText(magnitude.toString());
        depthText.setText(depth + depthMeasurement);
    }
}