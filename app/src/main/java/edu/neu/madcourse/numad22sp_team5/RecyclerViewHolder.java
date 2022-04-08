package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView babyName;
        ImageView babyPic;


    public RecyclerViewHolder(View itemView) {
            super(itemView);
            babyName = itemView.findViewById(R.id.name_baby);
            babyPic = itemView.findViewById(R.id.pic_baby);
        }
    }
