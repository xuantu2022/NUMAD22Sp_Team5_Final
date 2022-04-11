package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.eventTime.setText(currentEvent.getTime());
        holder.eventDescription.setText(currentEvent.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}