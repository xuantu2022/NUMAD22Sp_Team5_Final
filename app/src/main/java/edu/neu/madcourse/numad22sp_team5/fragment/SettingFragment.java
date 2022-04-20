package edu.neu.madcourse.numad22sp_team5.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEvent;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEventAdapter;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemMessageAdapter;
import edu.neu.madcourse.numad22sp_team5.GlobalStatus;
import edu.neu.madcourse.numad22sp_team5.MainActivity;
import edu.neu.madcourse.numad22sp_team5.R;
import edu.neu.madcourse.numad22sp_team5.StartActivity;


public class SettingFragment extends Fragment {
    private ArrayList<ItemEvent> eventList = new ArrayList<>();
    private String baby_id;
    private HashMap<String, String> post_id_to_publisher = new HashMap<>();
    private HashMap<String, String> publisher_id_to_name = new HashMap<>();
    private HashMap<String, String> user_id_to_name = new HashMap<>();
    private HashSet<String> post_id_list = new HashSet<>();

    private RecyclerView eventView;
    private ItemEventAdapter eventAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    GlobalStatus globalStatus;

    @Override
    public void onDestroy() {
        globalStatus.unmuteBaby(baby_id);
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("babyInfo",MODE_PRIVATE);
        baby_id = pref.getString("babyid","");

        creatSettingView(view);
        globalStatus = ((GlobalStatus) getActivity().getApplication());


        readTimeline();


        return view;
    }

    private void creatSettingView(View view) {
        eventView = view.findViewById(R.id.timeline);
        eventView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(getContext());
        eventView.setLayoutManager(rLayoutManger);
        eventAdapter = new ItemEventAdapter(getContext(), eventList);
        eventView.setAdapter(eventAdapter);
    }

    private void readTimeline() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                post_id_to_publisher.clear();
                publisher_id_to_name.clear();
                user_id_to_name.clear();
                post_id_list.clear();
                //get post snapshot
                for (DataSnapshot post_snapshot : snapshot.child("Posts").child(baby_id).getChildren()) {
                    String post_id = post_snapshot.getKey().toString();
                    String time = post_snapshot.child("time").getValue().toString();
                    String publisher = post_snapshot.child("publisher").getValue().toString();
                    String publisherName = snapshot.child("Users").child(publisher).child("username").getValue().toString();
                    String type = post_snapshot.child("postType").getValue().toString();
                    String description = post_snapshot.child("description").getValue().toString();
                    String postImage = post_snapshot.child("postImages").getValue().toString();
                    post_id_to_publisher.put(post_id, publisher);
                    publisher_id_to_name.put(publisher, publisherName);
                    user_id_to_name.put(publisher, publisherName);
                    post_id_list.add(post_id);
                    //add to eventList if the new post is from other user
                    if (!post_id_to_publisher.get(post_id).equals(firebaseUser.getUid())) {
                        eventList.add(0, new ItemEvent(baby_id, "New post from " + publisherName, time, publisher, type, description, post_id, postImage));
                    }
                }
                //get comment snapshot
                for (DataSnapshot comment_snapshot : snapshot.child("Comments").getChildren()) {
                    String post_id = comment_snapshot.getKey().toString();
                    //if the comment is for the current baby
                    if (post_id_list.contains(post_id)) {
                        for (DataSnapshot comments : comment_snapshot.getChildren()) {
                            String comment_detail = comments.child("comment").getValue().toString();
                            String comment_publisher = comments.child("publisher").getValue().toString();
                            String comment_time = comments.child("time").getValue().toString();
                            //add to eventList if the publisher of the post is the current user && the new comment is from other user
                            if (post_id_to_publisher.get(post_id).equals(firebaseUser.getUid()) && !comment_publisher.equals(firebaseUser.getUid())) {
                                eventList.add(0, new ItemEvent(baby_id, "New comment from " + publisher_id_to_name.get(comment_publisher), comment_time, comment_publisher, "", comment_detail, post_id, ""));
                            }
                        }
                    }
                }
                //get like snapshot
                for (DataSnapshot like_snapshot : snapshot.child("Likes").getChildren()) {
                    String post_id = like_snapshot.getKey().toString();
                    //if the comment is for the current baby
                    if (post_id_list.contains(post_id)) {
                        for (DataSnapshot users : like_snapshot.getChildren()) {
                            String user_id = users.getKey().toString();
                            String like_time = users.child("time").getValue().toString();
                            //add to eventList if the publisher of the post is the current user && the new like is from other user
                            if (post_id_to_publisher.get(post_id).equals(firebaseUser.getUid()) && !user_id.equals(firebaseUser.getUid())) {
                                eventList.add(0, new ItemEvent(baby_id, "New like from " + user_id_to_name.get(user_id), like_time, user_id, "", "", post_id, ""));
                            }
                        }
                    }
                }
                eventList.sort((ItemEvent o1, ItemEvent o2)->o2.getTime().compareToIgnoreCase(o1.getTime()));
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}