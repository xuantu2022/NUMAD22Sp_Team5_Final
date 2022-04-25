package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad22sp_team5.R;

public class ItemEventHolder extends RecyclerView.ViewHolder {
    public TextView eventTime;
    public TextView eventPublisher;
    public TextView eventDescription;
    public ImageView postImage;

    public ItemEventHolder(View itemView) {
        super(itemView);
        eventTime = itemView.findViewById(R.id.event_time);
        eventPublisher = itemView.findViewById(R.id.event_publisher);
        eventDescription = itemView.findViewById(R.id.event_description);
        postImage = itemView.findViewById(R.id.post_image);
    }
}