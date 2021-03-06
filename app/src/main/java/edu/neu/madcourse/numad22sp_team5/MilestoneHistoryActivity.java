package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.neu.madcourse.numad22sp_team5.Model.Post;
import edu.neu.madcourse.numad22sp_team5.Model.User;

public class MilestoneHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MilestoneHistoryAdapter adapter;
    ArrayList<Post> list;
    FirebaseAuth mAuth;
    TextView addMilestone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_history);

        // get current babyid
//        Intent intent = getIntent();
//        String babyid = intent.getStringExtra("babyid");

        SharedPreferences babyPref = getSharedPreferences("babyInfo", MODE_PRIVATE);
        String babyid = babyPref.getString("babyid", "");

        // get userid
        mAuth = FirebaseAuth.getInstance();
        String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        // add -> connect to addItemActivity
        addMilestone = findViewById(R.id.textView_addMilestone);
        addMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), AddItemActivity.class);
                //intent1.putExtra("babyid", babyid);
                startActivity(intent1);
            }
        });

        recyclerView = findViewById(R.id.recyclerView_milestone);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // check if current user follows baby
        String path1 = "Follow/" + userid;
        database = FirebaseDatabase.getInstance().getReference(path1);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!snapshot.child(babyid).exists()) {
                        Toast.makeText(getApplicationContext(), "You have no access to baby's Milestone", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // post - milestone
        String path2 = "Posts/" + babyid;
        database = FirebaseDatabase.getInstance().getReference(path2);
        list = new ArrayList<>();
        adapter = new MilestoneHistoryAdapter(this, list);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (Objects.requireNonNull(post).getPostType().equals("milestone") && post.getTag().length() > 0) {
                        list.add(post);
                    }
                }
                Collections.sort(list, (post1, post2) -> {
                    String t1 = post1.getTime();
                    String t2 = post2.getTime();
                    return t2.compareTo(t1);
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}