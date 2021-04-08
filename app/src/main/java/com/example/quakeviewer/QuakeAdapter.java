package com.example.quakeviewer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuakeAdapter extends RecyclerView.Adapter<QuakeAdapter.ViewHolder> {

    private List<QuakeItem> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private QuakeItem quake;
        private final CardView card;
        private final TextView textLocation;
        private final TextView textMagnitude;

        public ViewHolder(View view) {
            super(view);
            quake = null;
            // Define click listener for the ViewHolder's View
            card = (CardView) view.findViewById(R.id.quake_card);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), QuakeDetails.class);
                    intent.putExtra("origin", getItem().getOrigin().getTime());
                    intent.putExtra("location", getItem().getLocation());
                    intent.putExtra("depth", getItem().getDepth());
                    intent.putExtra("depthMeasurement", getItem().getDepthMeasurement());
                    intent.putExtra("latitude", getItem().getLat());
                    intent.putExtra("longitude", getItem().getLong());
                    intent.putExtra("magnitude", getItem().getMag());
                    view.getContext().startActivity(intent);
                }
            });
            textLocation = (TextView) view.findViewById(R.id.quake_location);
            textMagnitude = (TextView) view.findViewById(R.id.quake_magnitude);
        }

        public TextView getLocation() {
            return textLocation;
        }

        public TextView getMagnitude() {
            return textMagnitude;
        }

        public QuakeItem getItem() { return this.quake; }
        public void setItem(QuakeItem quake) { this.quake = quake; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public QuakeAdapter(List<QuakeItem> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        final QuakeItem quake = localDataSet.get(position);
        viewHolder.getLocation().setText(quake.getLocation());
        viewHolder.getMagnitude().setText(quake.getMag().toString());
        viewHolder.setItem(quake);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
