package com.example.quakeviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class QuakeDateFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_date_filter);
        Intent intent = getIntent();
        ArrayList<QuakeItem> quakeData = (ArrayList<QuakeItem>) intent.getSerializableExtra("QuakeData");

        TextView northQuakeLocation = findViewById(R.id.north_quake_location);
        TextView northQuakeMagnitude = findViewById(R.id.north_quake_magnitude);
        TextView southQuakeLocation = findViewById(R.id.south_quake_location);
        TextView southQuakeMagnitude = findViewById(R.id.south_quake_magnitude);
        TextView westQuakeLocation = findViewById(R.id.west_quake_location);
        TextView westQuakeMagnitude = findViewById(R.id.west_quake_magnitude);
        TextView eastQuakeLocation = findViewById(R.id.east_quake_location);
        TextView eastQuakeMagnitude = findViewById(R.id.east_quake_magnitude);
        TextView largestQuakeLocation = findViewById(R.id.largest_quake_location);
        TextView largestQuakeMagnitude = findViewById(R.id.largest_quake_magnitude);
        TextView deepestQuakeLocation = findViewById(R.id.deepest_quake_location);
        TextView deepestQuakeMagnitude = findViewById(R.id.deepest_quake_magnitude);
        TextView shallowestQuakeLocation = findViewById(R.id.shallowest_quake_location);
        TextView shallowestQuakeMagnitude = findViewById(R.id.shallowest_quake_magnitude);

        northQuakeLocation.setText(quakeData.get(0).getLocation());
        northQuakeMagnitude.setText(quakeData.get(0).getMag().toString());
        southQuakeLocation.setText(quakeData.get(1).getLocation());
        southQuakeMagnitude.setText(quakeData.get(1).getMag().toString());
        westQuakeLocation.setText(quakeData.get(2).getLocation());
        westQuakeMagnitude.setText(quakeData.get(2).getMag().toString());
        eastQuakeLocation.setText(quakeData.get(3).getLocation());
        eastQuakeMagnitude.setText(quakeData.get(3).getMag().toString());
        largestQuakeLocation.setText(quakeData.get(4).getLocation());
        largestQuakeMagnitude.setText(quakeData.get(4).getMag().toString());
        deepestQuakeLocation.setText(quakeData.get(5).getLocation());
        deepestQuakeMagnitude.setText(quakeData.get(5).getMag().toString());
        shallowestQuakeLocation.setText(quakeData.get(6).getLocation());
        shallowestQuakeMagnitude.setText(quakeData.get(6).getMag().toString());

        CardView northQuakeCard = (CardView) findViewById(R.id.north_quake_card);
        CardView southQuakeCard = (CardView) findViewById(R.id.south_quake_card);
        CardView westQuakeCard = (CardView) findViewById(R.id.west_quake_card);
        CardView eastQuakeCard = (CardView) findViewById(R.id.east_quake_card);
        CardView largestQuakeCard = (CardView) findViewById(R.id.largest_quake_card);
        CardView deepestQuakeCard = (CardView) findViewById(R.id.deepest_quake_card);
        CardView shallowestQuakeCard = (CardView) findViewById(R.id.shallowest_quake_card);

        northQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(0)));
        southQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(1)));
        westQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(2)));
        eastQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(3)));
        largestQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(4)));
        deepestQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(5)));
        shallowestQuakeCard.setOnClickListener(getOnClickListener(quakeData.get(6)));
    }

    private View.OnClickListener getOnClickListener(QuakeItem item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), QuakeDetails.class);
                intent.putExtra("origin", item.getOrigin().getTime());
                intent.putExtra("location", item.getLocation());
                intent.putExtra("depth", item.getDepth());
                intent.putExtra("depthMeasurement", item.getDepthMeasurement());
                intent.putExtra("latitude", item.getLat());
                intent.putExtra("longitude", item.getLong());
                intent.putExtra("magnitude", item.getMag());
                view.getContext().startActivity(intent);
            }
        };
    }
}