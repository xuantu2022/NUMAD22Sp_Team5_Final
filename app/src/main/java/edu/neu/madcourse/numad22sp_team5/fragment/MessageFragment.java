package edu.neu.madcourse.numad22sp_team5.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEvent;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEventAdapter;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemMessage;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemMessageAdapter;
import edu.neu.madcourse.numad22sp_team5.GlobalStatus;
import edu.neu.madcourse.numad22sp_team5.R;
import edu.neu.madcourse.numad22sp_team5.SnapshotParser;


public class MessageFragment extends Fragment {
    private ArrayList<ItemEvent> eventList = new ArrayList<>();
    private String baby_id;
//    private HashMap<String, String> post_id_to_publisher = new HashMap<>();
//    private HashMap<String, String> publisher_id_to_name = new HashMap<>();
//    private HashMap<String, String> user_id_to_name = new HashMap<>();
//    private HashSet<String> post_id_list = new HashSet<>();

    private RecyclerView eventView;
    private ItemEventAdapter eventAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    GlobalStatus globalStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("info", "Creating message fragment");
        SharedPreferences pref = getActivity().getSharedPreferences("babyInfo",MODE_PRIVATE);
        baby_id = pref.getString("babyid","");

        readTimeline();

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        createMessageView(view);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        globalStatus = ((GlobalStatus) getActivity().getApplication());
        globalStatus.setMessageRunning(true);
        //test = ((GlobalStatus) getActivity().getApplication()).getTest();
        // addMessage(0, "System notification", "nickname");
        return view;
    }



    private void createMessageView(View view) {
        eventView = view.findViewById(R.id.messages);
        eventView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(getContext());
        eventView.setLayoutManager(rLayoutManger);
        eventAdapter = new ItemEventAdapter(getContext(), eventList);
        eventView.setAdapter(eventAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("info", "destroying message fragment");
        globalStatus.setMessageRunning(false);
        globalStatus.unmuteBaby(baby_id);
    }

    private void readTimeline() {
        String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
//                post_id_to_publisher.clear();
//                publisher_id_to_name.clear();
//                user_id_to_name.clear();
//                post_id_list.clear();
                //get post snapshot
                for (DataSnapshot notify_snapshot : snapshot.child("Notification").child(baby_id).getChildren()) {
                    String notification_id = notify_snapshot.getKey().toString();
                    String post_id = notify_snapshot.child("postid").getValue().toString();
                    String time = notify_snapshot.child("time").getValue().toString();
                    String publisher = notify_snapshot.child("publisher").getValue().toString();
                    String publisherName = snapshot.child("Users").child(publisher).child("username").getValue().toString();
                    String type = notify_snapshot.child("type").getValue().toString();
                    String description = notify_snapshot.child("description").getValue().toString();
                    String postImage = notify_snapshot.child("postImage").getValue().toString();
                    String postPublisher = notify_snapshot.child("post publisher").getValue().toString();
                    //post_id_to_publisher.put(post_id, publisher);
                    //publisher_id_to_name.put(publisher, publisherName);
                    //user_id_to_name.put(publisher, publisherName);
                    //post_id_list.add(post_id);
                    //add to eventList if the new post is from other user
                    if (type.equals("post") && !publisher.equals(firebaseUser)) {
                        eventList.add(0, new ItemEvent(baby_id, "New post from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
                    }
                    if (type.equals("comment") && postPublisher.equals(firebaseUser) && !publisher.equals(firebaseUser)) {
                        eventList.add(0, new ItemEvent(baby_id, "New comment from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
                    }
                    if (type.equals("like") && postPublisher.equals(firebaseUser) && !publisher.equals(firebaseUser)) {
                        eventList.add(0, new ItemEvent(baby_id, "New like from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
                    }

//                    if (type.equals("post") && !post_id_to_publisher.get(post_id).equals(firebaseUser.getUid())) {
//                        eventList.add(0, new ItemEvent(baby_id, "New post from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
//                    }
//                    if (type.equals("comment") && post_id_to_publisher.get(post_id).equals(firebaseUser.getUid()) && !publisher.equals(firebaseUser.getUid())) {
//                        eventList.add(0, new ItemEvent(baby_id, "New comment from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
//                    }
//                    if (type.equals("like") && post_id_to_publisher.get(post_id).equals(firebaseUser.getUid()) && !publisher.equals(firebaseUser.getUid())) {
//                        eventList.add(0, new ItemEvent(baby_id, "New like from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
//                    }

                }
//                //get comment snapshot
//                for (DataSnapshot comment_snapshot : snapshot.child("Comments").getChildren()) {
//                    String post_id = comment_snapshot.getKey().toString();
//                    //if the comment is for the current baby
//                    if (post_id_list.contains(post_id)) {
//                        for (DataSnapshot comments : comment_snapshot.getChildren()) {
//                            String comment_detail = comments.child("comment").getValue().toString();
//                            String comment_publisher = comments.child("publisher").getValue().toString();
//                            String comment_time = comments.child("time").getValue().toString();
//                            //add to eventList if the publisher of the post is the current user && the new comment is from other user
//                            if (post_id_to_publisher.get(post_id).equals(firebaseUser.getUid()) && !comment_publisher.equals(firebaseUser.getUid())) {
//                                eventList.add(0, new ItemEvent(baby_id, "New comment from " + publisher_id_to_name.get(comment_publisher), comment_time, comment_publisher, "", comment_detail, post_id, ""));
//                            }
//                        }
//                    }
//                }
//                //get like snapshot
//                for (DataSnapshot like_snapshot : snapshot.child("Likes").getChildren()) {
//                    String post_id = like_snapshot.getKey().toString();
//                    //if the comment is for the current baby
//                    if (post_id_list.contains(post_id)) {
//                        for (DataSnapshot users : like_snapshot.getChildren()) {
//                            String user_id = users.getKey().toString();
//                            String like_time = users.child("time").getValue().toString();
//                            //add to eventList if the publisher of the post is the current user && the new like is from other user
//                            if (post_id_to_publisher.get(post_id).equals(firebaseUser.getUid()) && !user_id.equals(firebaseUser.getUid())) {
//                                eventList.add(0, new ItemEvent(baby_id, "New like from " + user_id_to_name.get(user_id), like_time, user_id, "", "", post_id, ""));
//                            }
//                        }
//                    }
//                }
                eventList.sort((ItemEvent o1, ItemEvent o2)->o2.getTime().compareToIgnoreCase(o1.getTime()));
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    private void addMessage(int position, String babyId, String nickname) {
//        messages.add(position, new ItemMessage(babyId, nickname, headshot));
//        messageAdapter.notifyItemInserted(position);
//    }
}