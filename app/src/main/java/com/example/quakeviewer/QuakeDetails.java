package com.example.quakeviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuakeDetails extends AppCompatActivity {
    private MapView quakeMapView;
    private Toolbar toolbar;

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

        toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        toolbar.setTitle(R.string.info_title);
        setSupportActionBar(toolbar);

        TextView dateText = findViewById(R.id.info_date);
        TextView locationText = findViewById(R.id.info_location);
        TextView magnitudeText = findViewById(R.id.info_magnitude);
        TextView depthText = findViewById(R.id.info_depth);

        Date originDate = new Date(origin);
        SimpleDateFormat originFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String originDateText = originFormat.format(originDate);

        dateText.setText(originDateText);
        locationText.setText(location);
        magnitudeText.setText(magnitude.toString());
        depthText.setText(depth + depthMeasurement);

        LatLng quakeCoords = new LatLng(latitude, longitude);
        quakeMapView = (MapView) findViewById(R.id.info_map_view);
        final GoogleMap[] quakeMap = new GoogleMap[1];

        quakeMapView.onCreate(null);
        quakeMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions quakeMarkerOptions = new MarkerOptions().position(quakeCoords).title(originDateText);
                quakeMarkerOptions.icon(CustomBitmapDescriptorFactory.fromColorString(getMagnitudeColor(magnitude)));
                quakeMap[0] = googleMap;
                quakeMap[0].getUiSettings().setZoomControlsEnabled(true);
                quakeMap[0].getUiSettings().setZoomGesturesEnabled(true);
                quakeMap[0].getUiSettings().setCompassEnabled(true);
                quakeMap[0].addMarker(quakeMarkerOptions);
                quakeMap[0].moveCamera(CameraUpdateFactory.newLatLng(quakeCoords));
            }
        });

        MapsInitializer.initialize(quakeMapView.getContext());
    }


    @Override
    public void onResume() {
        quakeMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quakeMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        quakeMapView.onLowMemory();
    }

    private String getMagnitudeColor(Double magnitude) {
        if (magnitude < 2.0)
            return "#" + Integer.toHexString(getResources().getColor(R.color.quake_not_felt));
        if (magnitude < 3.8)
            return "#" + Integer.toHexString(getResources().getColor(R.color.quake_weak));
        if (magnitude < 6.5)
            return "#" + Integer.toHexString(getResources().getColor(R.color.quake_moderate));
        if (magnitude < 8.5)
            return "#" + Integer.toHexString(getResources().getColor(R.color.quake_very_strong));
        else
            return "#" + Integer.toHexString(getResources().getColor(R.color.quake_violent));
    }
}