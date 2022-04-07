package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class GrowthHistoryAdapter extends RecyclerView.Adapter<GrowthHistoryAdapter.GrowthHistoryViewHolder> {
    ArrayList<Growth> list;
    Context context;

    public GrowthHistoryAdapter(ArrayList<Growth> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public GrowthHistoryAdapter.GrowthHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_growth, parent, false);
        return new GrowthHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthHistoryAdapter.GrowthHistoryViewHolder holder, int position) {
        Growth growth = list.get(position);
        holder.date.setText(growth.getDate());
        holder.height.setText(growth.getHeight());
        holder.weight.setText(growth.getWeight());
        holder.headCirc.setText(growth.getHeadCirc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GrowthHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView height;
        TextView weight;
        TextView headCirc;

        public GrowthHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView_date_growth);
            height = itemView.findViewById(R.id.textView_height_growth);
            weight = itemView.findViewById(R.id.textView_weight_growth);
            headCirc = itemView.findViewById(R.id.textView_headCirc_growth);
        }
    }
}
