package edu.neu.madcourse.numad22sp_team5.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import java.util.Objects;

import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEvent;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEventAdapter;
import edu.neu.madcourse.numad22sp_team5.GlobalStatus;
import edu.neu.madcourse.numad22sp_team5.R;


public class MessageFragment extends Fragment {
    private ArrayList<ItemEvent> eventList = new ArrayList<>();
    private String baby_id;
    private RecyclerView eventView;
    private ItemEventAdapter eventAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    GlobalStatus globalStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences pref = requireActivity().getSharedPreferences("babyInfo",MODE_PRIVATE);
        baby_id = pref.getString("babyid","");

        readTimeline();

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        createMessageView(view);
        globalStatus = ((GlobalStatus) requireActivity().getApplication());
        globalStatus.setMessageRunning(true);
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
        String firebaseUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot notify_snapshot : snapshot.child("Notification").child(baby_id).getChildren()) {
                    String notification_id = Objects.requireNonNull(notify_snapshot.getKey()).toString();
                    String post_id = Objects.requireNonNull(notify_snapshot.child("postid").getValue()).toString();
                    String time = Objects.requireNonNull(notify_snapshot.child("time").getValue()).toString();
                    String publisher = Objects.requireNonNull(notify_snapshot.child("publisher").getValue()).toString();
                    String publisherName = Objects.requireNonNull(snapshot.child("Users").child(publisher).child("username").getValue()).toString();
                    String type = Objects.requireNonNull(notify_snapshot.child("type").getValue()).toString();
                    String description = Objects.requireNonNull(notify_snapshot.child("description").getValue()).toString();
                    String postImage = Objects.requireNonNull(notify_snapshot.child("postImage").getValue()).toString();
                    String postPublisher = Objects.requireNonNull(notify_snapshot.child("post publisher").getValue()).toString();
                    if (type.equals("post") && !publisher.equals(firebaseUser)) {
                        eventList.add(0, new ItemEvent(baby_id, "New post from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
                    }
                    if (type.equals("comment") && postPublisher.equals(firebaseUser) && !publisher.equals(firebaseUser)) {
                        eventList.add(0, new ItemEvent(baby_id, "New comment from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
                    }
                    if (type.equals("like") && postPublisher.equals(firebaseUser) && !publisher.equals(firebaseUser)) {
                        eventList.add(0, new ItemEvent(baby_id, "New like from " + publisherName, time, publisher, type, description, post_id, postPublisher, postImage));
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