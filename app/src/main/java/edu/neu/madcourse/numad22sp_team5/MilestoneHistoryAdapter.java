package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.Model.Post;

public class MilestoneHistoryAdapter extends RecyclerView.Adapter<MilestoneHistoryAdapter.MilestoneHistoryViewHolder> {
    ArrayList<Post> list;
    Context context;

    public MilestoneHistoryAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MilestoneHistoryAdapter.MilestoneHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("db-debug", "a0");
        View view = LayoutInflater.from(context).inflate(R.layout.item_milestone, parent, false);
        Log.i("db-debug", "a1");
        return new MilestoneHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneHistoryAdapter.MilestoneHistoryViewHolder holder, int position) {
        Log.i("db-debug", "a2");
        Post milestonePost = list.get(position);
        Log.i("db-debug", "a3");
        holder.timeMilestone.setText(milestonePost.getTime());
        holder.tagMilestone.setText(milestonePost.getTag());
        // fetch image
        Log.i("db-debug", "a4");
        String url = milestonePost.getPostImages();
        Log.i("db-debug", "a5");
        Glide.with(context).load(url).centerCrop().into(holder.imageMilestone);
        Log.i("db-debug", "a6");
    }

    @Override
    public int getItemCount() {
        Log.i("db-debug", "a7");
        return list.size();
    }

    public static class MilestoneHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView timeMilestone;
        TextView tagMilestone;
        ImageView imageMilestone;

        public MilestoneHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("db-debug", "a8");
            timeMilestone = itemView.findViewById(R.id.textView_time_milestone);
            tagMilestone = itemView.findViewById(R.id.textView_tag_milestone);
            imageMilestone = itemView.findViewById(R.id.imageView_image_milestone);
            Log.i("db-debug", "a9");
        }
    }
}
