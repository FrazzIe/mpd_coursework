package com.example.quakeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class QuakeDateFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_date_filter);
        Intent intent = getIntent();
        ArrayList<QuakeItem> quakeData = (ArrayList<QuakeItem>) intent.getSerializableExtra("QuakeData");
    }
}