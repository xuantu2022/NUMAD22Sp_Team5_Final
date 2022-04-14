package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.PostDetailActivity;
import edu.neu.madcourse.numad22sp_team5.R;

public class ItemEventAdapter extends RecyclerView.Adapter<ItemEventHolder> {
    private Context mContext;
    private ArrayList<ItemEvent> eventList;

    //Constructor
    public ItemEventAdapter(Context context, ArrayList<ItemEvent> itemList) {
        this.mContext = context;
        this.eventList = itemList;
    }

    @Override
    public ItemEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, parent, false);
        return new ItemEventHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemEventHolder holder, int position) {
        ItemEvent currentEvent = eventList.get(position);
//        holder.eventType.setText("Post type: " + currentEvent.getType());
//        if (currentEvent.getType().length() == 0) {
//            holder.eventType.setVisibility(View.GONE);
//        }
        holder.eventTime.setText(currentEvent.getTime());
        holder.eventPublisher.setText(currentEvent.getPublisherName());
        holder.eventDescription.setText("\"" + currentEvent.getDescription() + "\"");
        if (currentEvent.getDescription().length() == 0){
            holder.eventDescription.setVisibility(View.GONE);
        }
        Glide.with(mContext).load(currentEvent.getPostImage()).centerCrop().into(holder.postImage);
        if (currentEvent.getPostImage().length() == 0) {
            holder.postImage.setVisibility(View.GONE);
        }

        ItemEvent event = eventList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("postid", event.getPostId());
                intent.putExtra("babyid", event.getBabyid());
                intent.putExtra("publisherid", event.getPublisher());
                mContext.startActivity(intent);
            }
        });

//        holder.eventType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PostDetailActivity.class);
//                intent.putExtra("postid", event.getPostId());
//                intent.putExtra("babyid", event.getBabyid());
//                intent.putExtra("publisherid", event.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
//
//        holder.eventPublisher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PostDetailActivity.class);
//                intent.putExtra("postid", event.getPostId());
//                intent.putExtra("babyid", event.getBabyid());
//                intent.putExtra("publisherid", event.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
//
//        holder.eventTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PostDetailActivity.class);
//                intent.putExtra("postid", event.getPostId());
//                intent.putExtra("babyid", event.getBabyid());
//                intent.putExtra("publisherid", event.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}