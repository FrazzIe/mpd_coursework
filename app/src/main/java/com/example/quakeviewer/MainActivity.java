package com.example.quakeviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView uiRecyclerView = (RecyclerView) findViewById(R.id.quake_list);
        RecyclerView.LayoutManager uiRecyclerLayout = new GridLayoutManager(this, 1);
        uiRecyclerView.setLayoutManager(uiRecyclerLayout);

        Log.d("INFO", "STARTED");
        URL feed = null;

        try {
            feed = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        QuakeList quakeList = new QuakeList(feed, uiRecyclerView);
    }
}