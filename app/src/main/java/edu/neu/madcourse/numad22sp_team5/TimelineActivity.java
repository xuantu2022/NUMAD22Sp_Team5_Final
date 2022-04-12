package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEvent;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEventAdapter;

public class TimelineActivity extends AppCompatActivity {
    private String baby_id;
    private HashMap<String, String> post_id_to_publisher = new HashMap<>();
    private HashMap<String, String> publisher_id_to_name = new HashMap<>();
    private HashMap<String, String> user_id_to_name = new HashMap<>();
    private HashSet<String> post_id_list = new HashSet<>();
    private ArrayList<ItemEvent> eventList = new ArrayList<>();

    private RecyclerView eventView;
    private ItemEventAdapter eventAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_timeline);

        Intent intent = getIntent();
        baby_id = intent.getStringExtra("baby_id");

        eventView = findViewById(R.id.timeline);
        eventView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(this);
        eventView.setLayoutManager(rLayoutManger);
        eventAdapter = new ItemEventAdapter(this, eventList);
        eventView.setAdapter(eventAdapter);

        readTimeline();
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
                for (DataSnapshot data : snapshot.child("Posts").child(baby_id).getChildren()) {
                    String post_id = data.getKey().toString();
                    String time = data.child("time").getValue().toString();
                    String publisher = data.child("publisher").getValue().toString();
                    String publisherName = snapshot.child("Users").child(publisher).child("username").getValue().toString();
                    String type = data.child("postType").getValue().toString();
                    String description = data.child("description").getValue().toString();
                    post_id_to_publisher.put(post_id, publisher);
                    publisher_id_to_name.put(publisher, publisherName);
                    user_id_to_name.put(publisher, publisherName);
                    post_id_list.add(post_id);
                    eventList.add(0, new ItemEvent(baby_id, "publisher: " + publisherName, time, publisher, "type: post", "description: " +description, post_id));
                }
                for (DataSnapshot comment_snapshot : snapshot.child("Comments").getChildren()) {
                    String post_id = comment_snapshot.getKey().toString();
                    // if(post_id_to_publisher.get(post_id) == firebaseUser.getUid())
                    if (post_id_list.contains(post_id)) {
                        for (DataSnapshot comments : comment_snapshot.getChildren()) {
                            String comment_detail = comments.child("comment").getValue().toString();
                            String comment_publisher = comments.child("publisher").getValue().toString();
                            eventList.add(0, new ItemEvent(baby_id, "Publisher: " + publisher_id_to_name.get(comment_publisher), "no time", comment_publisher, "type: comments", "comment detail: " + comment_detail, post_id ));
                        }
                    }
                }
                for (DataSnapshot like_snapshot : snapshot.child("Likes").getChildren()) {
                    String post_id = like_snapshot.getKey().toString();
                    // if (post_id_to_publisher.get(post_id) == firebaseUser.getUid())
                    if (post_id_list.contains(post_id)) {
                        for (DataSnapshot users : like_snapshot.getChildren()) {
                            String user_id = users.getKey().toString();
                            eventList.add(0, new ItemEvent(baby_id, "Liked by: " + user_id_to_name.get(user_id), "no time", user_id, "type: like", "user " + user_id_to_name.get(user_id) + " liked your post", post_id));
                        }
                    }
                }
                // TODO: sort by time.
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
