package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.MainActivity;
import edu.neu.madcourse.numad22sp_team5.PostDetailActivity;
import edu.neu.madcourse.numad22sp_team5.R;
import edu.neu.madcourse.numad22sp_team5.TimelineActivity;

public class ItemMessageAdapter extends RecyclerView.Adapter<ItemMessageHolder> {
    private Context mContext;
    private ArrayList<ItemMessage> itemList;
    private Long currentCount;


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
        holder.nickName.setText(currentItem.getNickname());
        holder.unread.setVisibility(View.GONE);


        Glide.with(mContext).load(currentItem.getHeadshot()).centerCrop().into(holder.headshot);

        ItemMessage message = itemList.get(position);
        String baby_id = message.getBabyId();
        DatabaseReference post_reference = FirebaseDatabase.getInstance().getReference("Posts");
        post_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long posts_count = snapshot.child(baby_id).getChildrenCount();
                if (!holder.onCreate && posts_count > currentCount) {
                    holder.unread.setVisibility(View.VISIBLE);
                }
                currentCount = posts_count;
                holder.onCreate = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.unread.setVisibility(View.GONE);
                Intent intent = new Intent(mContext, TimelineActivity.class);
                intent.putExtra("baby_id", message.getBabyId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
