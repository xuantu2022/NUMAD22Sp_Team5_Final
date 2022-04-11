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
import edu.neu.madcourse.numad22sp_team5.TimelineActivity;

public class ItemMessageAdapter extends RecyclerView.Adapter<ItemMessageHolder> {
    private Context mContext;
    private ArrayList<ItemMessage> itemList;

    //Constructor
    public ItemMessageAdapter(Context context, ArrayList<ItemMessage> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    @Override
    public ItemMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new ItemMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemMessageHolder holder, int position) {
        ItemMessage currentItem = itemList.get(position);
        holder.messageName.setText(currentItem.getMessageName());

        ItemMessage message = itemList.get(position);

        holder.messageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TimelineActivity.class);
                intent.putExtra("baby_id", message.getMessageName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
