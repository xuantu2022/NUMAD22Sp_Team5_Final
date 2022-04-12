package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad22sp_team5.R;

public class ItemMessageHolder extends RecyclerView.ViewHolder {
    public ImageView headshot;
    public TextView nickName;
    public ImageView unread;
    public boolean onCreate = true;

    public ItemMessageHolder(View itemView) {
        super(itemView);
        headshot = itemView.findViewById(R.id.pic_baby);
        nickName = itemView.findViewById(R.id.nick_name);
        unread = itemView.findViewById(R.id.image_unread);
    }
}
