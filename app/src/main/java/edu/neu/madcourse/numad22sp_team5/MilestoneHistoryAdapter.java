package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_milestone, parent, false);
        return new MilestoneHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneHistoryAdapter.MilestoneHistoryViewHolder holder, int position) {
        Post milestonePost = list.get(position);
        holder.timeMilestone.setText(milestonePost.getTime());
        holder.tagMilestone.setText(milestonePost.getTag());
        // fetch image
        String url = milestonePost.getPostImages();
        Glide.with(context).load(url).centerCrop().into(holder.imageMilestone);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MilestoneHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView timeMilestone;
        TextView tagMilestone;
        ImageView imageMilestone;

        public MilestoneHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            timeMilestone = itemView.findViewById(R.id.textView_time_milestone);
            tagMilestone = itemView.findViewById(R.id.textView_tag_milestone);
            imageMilestone = itemView.findViewById(R.id.imageView_image_milestone);
        }
    }
}
