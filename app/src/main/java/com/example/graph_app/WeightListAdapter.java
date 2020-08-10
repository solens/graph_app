package com.example.graph_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeightListAdapter extends RecyclerView.Adapter<WeightListAdapter.WeightViewHolder> {

    class WeightViewHolder extends RecyclerView.ViewHolder {
        private final TextView weightItemView;


        private WeightViewHolder(View itemView) {
            super(itemView);
            weightItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<DatedPoint> datedPointList; // Cached copy of words

    WeightListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WeightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WeightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeightViewHolder holder, int position) {
        if (datedPointList != null) {
            DatedPoint current = datedPointList.get(position);
            holder.weightItemView.setText((current.toString()));
        } else {
            // Covers the case of data not being ready yet.
            holder.weightItemView.setText("No Weights");
        }
    }

    void setWeights(List<DatedPoint> weights){
        datedPointList = weights;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (datedPointList != null)
            return datedPointList.size();
        else return 0;
    }
}