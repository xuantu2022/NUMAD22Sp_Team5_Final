package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad22sp_team5.R;

public class ItemMessageHolder extends RecyclerView.ViewHolder {
    public TextView messageName;

    public ItemMessageHolder(View itemView) {
        super(itemView);
        messageName = itemView.findViewById(R.id.message_name);
    }
}
