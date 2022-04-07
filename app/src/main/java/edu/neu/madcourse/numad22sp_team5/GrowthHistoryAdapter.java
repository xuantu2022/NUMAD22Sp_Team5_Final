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
    ArrayList<Post> list;
    Context context;

    public GrowthHistoryAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GrowthHistoryAdapter.GrowthHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_growth, parent, false);
        return new GrowthHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthHistoryAdapter.GrowthHistoryViewHolder holder, int position) {
        Post growthPost = list.get(position);
        holder.date.setText(growthPost.getTime());
//        holder.height.setText(growth.getHeight());
//        holder.weight.setText(growth.getWeight());
//        holder.headCirc.setText(growth.getHeadCirc());
        holder.babyinfo.setText(growthPost.getGrowth());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GrowthHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView date;
//        TextView height;
//        TextView weight;
//        TextView headCirc;
        TextView babyinfo;

        public GrowthHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView_date_growth);
//            height = itemView.findViewById(R.id.textView_height_growth);
//            weight = itemView.findViewById(R.id.textView_weight_growth);
//            headCirc = itemView.findViewById(R.id.textView_headCirc_growth);
            babyinfo = itemView.findViewById(R.id.textView_babyinfo_growth);
        }
    }
}
