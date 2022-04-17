package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.neu.madcourse.numad22sp_team5.GlobalStatus;
import edu.neu.madcourse.numad22sp_team5.MainActivity;
import edu.neu.madcourse.numad22sp_team5.PostDetailActivity;
import edu.neu.madcourse.numad22sp_team5.R;
import edu.neu.madcourse.numad22sp_team5.SnapshotParser;
import edu.neu.madcourse.numad22sp_team5.TimelineActivity;

public class ItemMessageAdapter extends RecyclerView.Adapter<ItemMessageHolder> {
    private Context mContext;
    private ArrayList<ItemMessage> itemList;
    private Long currentCount;
    private SnapshotParser snapshotParser;

    //Constructor
    public ItemMessageAdapter(Context context, ArrayList<ItemMessage> itemList) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        snapshotParser = new SnapshotParser(firebaseUser.getUid());
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
        // holder.unread.setVisibility(View.GONE);


        Glide.with(mContext).load(currentItem.getHeadshot()).centerCrop().into(holder.headshot);

        ItemMessage message = itemList.get(position);
        GlobalStatus status = message.getStatus();
        String baby_id = message.getBabyId();
        boolean shouldNotify = message.isNotifyOnCreate();
        if (shouldNotify && holder.onCreate) {
            holder.unread.setVisibility(View.VISIBLE);
        } else if (holder.onCreate) {
            holder.unread.setVisibility(View.GONE);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference post_reference = FirebaseDatabase.getInstance().getReference();
        post_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (holder.onCreate) {
                    // Log.d("info", "init message adapter received a data snapshot");
                    snapshotParser.parse(snapshot);
                    holder.onCreate = false;
                } else {
                    // Log.d("info", "message adapter received another data snapshot");

                    // check if there is any new post for this babies.
                    HashMap<String, Long> babyPostCount = snapshotParser.parseBabyPostCount(snapshot);
                    if (babyPostCount.get(baby_id) > snapshotParser.postCountForBaby(baby_id) && !status.isBabyMute(baby_id)) {
                        if (!snapshotParser.publisherOfBabyLastPost(snapshot, baby_id).equals(firebaseUser.getUid())) {
                            holder.unread.setVisibility(View.VISIBLE);
                        }
                        snapshotParser.setBabyPostCounter(babyPostCount);
                    }


                    // check if there is any new comments for baby's posts.
                    HashMap<String, Long> babyCommentCount = snapshotParser.parseBabyCommentCount(snapshot);
                    HashMap<String, Long> postCommentCount = snapshotParser.parsePostCommentCount(snapshot);
                    // Log.d("info", "there were " + snapshotParser.commentCountForBaby(baby_id) + " comments for baby " + baby_id);
                    if (babyCommentCount.get(baby_id) > snapshotParser.commentCountForBaby(baby_id) && !status.isBabyMute(baby_id)) {
                        //Log.d("info", "send nitofication for new comment " + baby_id);
                        for (String myPost : snapshotParser.myPosts(snapshot)) {
                            if (!postCommentCount.containsKey(myPost)) {
                                Log.d("warning", "unknow post in post table: " + myPost);
                                continue;
                            }
                            if (postCommentCount.get(myPost) > snapshotParser.commentCountForPost(myPost)) {
                                if (!snapshotParser.publisherOfLastCommentOnPost(snapshot, myPost).equals(firebaseUser.getUid())) {
                                    holder.unread.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        snapshotParser.setBabyCommentCounter(babyCommentCount);
                        snapshotParser.setPostCommentCounter(postCommentCount);
                    }


                    // Check if there is any new like for this baby's posts.
                    HashMap<String, Long> babyLikeCount = snapshotParser.parseBabyLikeCount(snapshot);
                    HashMap<String, Long> postLikeCount = snapshotParser.parsePostLikeCount(snapshot);
                    if (babyLikeCount.get(baby_id) > snapshotParser.likeCountForBaby(baby_id) && !status.isBabyMute(baby_id)) {
                        for (String myPost : snapshotParser.myPosts(snapshot)) {
                            if (!postLikeCount.containsKey(myPost)) {
                                Log.d("warning", "unknow post for like table: " + myPost);
                                continue;
                            }
                            if (postLikeCount.get(myPost) > snapshotParser.likeCountForPost(myPost)) {
                                if (!snapshotParser.publisherOfLastLikeOnPost(snapshot, myPost).equals(firebaseUser.getUid())) {
                                    holder.unread.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        snapshotParser.setBabyLikeCounter(babyLikeCount);
                        snapshotParser.setPostLikeCounter(postLikeCount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.unread.setVisibility(View.GONE);
                status.muteBaby(message.getBabyId());
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
