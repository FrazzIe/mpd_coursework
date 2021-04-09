package com.example.quakeviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

// S1916169 - Fraser Watt (Plagiarism check)
public class QuakeDetails extends AppCompatActivity {
    private MapView quakeMapView;
    private Toolbar toolbar;
    private final String infoTag = "QuakeDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_details);

        Log.i(infoTag, "Init details view");
        Log.i(infoTag, "Get passed data");
        Intent intent = getIntent();

        Log.i(infoTag, "Get quake detials");
        long origin = intent.getLongExtra("origin", 0);
        String location = intent.getStringExtra("location");
        int depth = intent.getIntExtra("depth", 0);
        String depthMeasurement = intent.getStringExtra("depthMeasurement");
        Double latitude = intent.getDoubleExtra("latitude", 0.0);
        Double longitude = intent.getDoubleExtra("longitude", 0.0);
        Double magnitude = intent.getDoubleExtra("magnitude", 0.0);

        toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        toolbar.setTitle(R.string.info_title);
        Log.i(infoTag, "Set toolbar as action bar");
        setSupportActionBar(toolbar);

        Log.i(infoTag, "Get all dynamic text views");
        TextView dateText = findViewById(R.id.info_date);
        TextView locationText = findViewById(R.id.info_location);
        TextView magnitudeText = findViewById(R.id.info_magnitude);
        TextView depthText = findViewById(R.id.info_depth);

        Date originDate = new Date(origin);
        SimpleDateFormat originFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String originDateText = originFormat.format(originDate);

        Log.i(infoTag, "Set text view dynamic data");
        dateText.setText(originDateText);
        locationText.setText(location);
        magnitudeText.setText(magnitude.toString());
        depthText.setText(depth + depthMeasurement);

        Log.i(infoTag, "Setup Google Map View");
        LatLng quakeCoords = new LatLng(latitude, longitude);
        quakeMapView = (MapView) findViewById(R.id.info_map_view);
        final GoogleMap[] quakeMap = new GoogleMap[1];

        quakeMapView.onCreate(null);
        Log.i(infoTag, "Request Map Data");
        quakeMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions quakeMarkerOptions = new MarkerOptions().position(quakeCoords).title(originDateText);
                quakeMarkerOptions.icon(CustomBitmapDescriptorFactory.fromColorString(getMagnitudeColor(magnitude)));
                quakeMap[0] = googleMap;
                Log.i(infoTag, "Enable map features");
                quakeMap[0].getUiSettings().setZoomControlsEnabled(true);
                quakeMap[0].getUiSettings().setZoomGesturesEnabled(true);
                quakeMap[0].getUiSettings().setCompassEnabled(true);
                Log.i(infoTag, "Add earthquake map marker");
                quakeMap[0].addMarker(quakeMarkerOptions);
                Log.i(infoTag, "Move map camera over to earthquake location");
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
        Log.i(infoTag, "Fetch quake magnitude colour");
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