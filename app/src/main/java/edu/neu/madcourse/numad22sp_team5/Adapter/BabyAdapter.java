package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.Adapter.BabyViewHolder;
import edu.neu.madcourse.numad22sp_team5.ItemClickListener;
import edu.neu.madcourse.numad22sp_team5.LinkItem;
import edu.neu.madcourse.numad22sp_team5.R;


public class BabyAdapter extends RecyclerView.Adapter<BabyViewHolder> {
    private ArrayList<LinkItem> mData;
    private ItemClickListener mListner;

    public BabyAdapter(ItemClickListener listener) {
        mData = new ArrayList<>();
        mListner = listener;
    }

    public void setItemClickListener(ItemClickListener listener) {
        mListner = listener;
    }

    public String getItemName(int pos) {
        return mData.get(pos).getName();
    }


    public void addNewItem(String name) {
        mData.add(0, new LinkItem(name));
        notifyItemInserted(0);
    }

    public void deleteItem(int pos) {
        if (mData.isEmpty()) {
            return;
        }
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public BabyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BabyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BabyViewHolder holder, int position) {
        holder.babyName.setText(mData.get(position).getName());

        if (mListner != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListner.onItemClick(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

