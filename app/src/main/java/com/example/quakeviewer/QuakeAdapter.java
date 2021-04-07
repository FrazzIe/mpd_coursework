package com.example.quakeviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuakeAdapter extends RecyclerView.Adapter<QuakeAdapter.ViewHolder> {

    private List<QuakeItem> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLocation;
        private final TextView textMagnitude;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textLocation = (TextView) view.findViewById(R.id.quake_location);
            textMagnitude = (TextView) view.findViewById(R.id.quake_magnitude);
        }

        public TextView getLocation() {
            return textLocation;
        }

        public TextView getMagnitude() {
            return textMagnitude;
        }
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
