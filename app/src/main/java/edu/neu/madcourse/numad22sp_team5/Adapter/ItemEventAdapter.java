package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

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
        holder.eventType.setText(currentEvent.getType());
        holder.eventTime.setText(currentEvent.getTime());
        holder.eventPublisher.setText(currentEvent.getPublisherName());
        holder.eventDescription.setText(currentEvent.getDescription());

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