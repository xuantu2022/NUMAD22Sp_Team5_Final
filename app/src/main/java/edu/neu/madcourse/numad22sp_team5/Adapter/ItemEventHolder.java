package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad22sp_team5.R;

public class ItemEventHolder extends RecyclerView.ViewHolder {
    public TextView eventTime;
    public TextView eventDescription;

    public ItemEventHolder(View itemView) {
        super(itemView);
        eventTime = itemView.findViewById(R.id.event_time);
        eventDescription = itemView.findViewById(R.id.event_description);
    }
}