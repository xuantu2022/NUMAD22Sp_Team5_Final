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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEvent;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemEventAdapter;

public class TimelineActivity extends AppCompatActivity {
    private String baby_id;
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(baby_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    eventList.add(0, new ItemEvent("time", "description"));
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
