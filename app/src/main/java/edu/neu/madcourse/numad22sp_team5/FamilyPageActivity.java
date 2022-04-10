package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.numad22sp_team5.Model.User;

public class FamilyPageActivity extends AppCompatActivity {

    TextView family_add;
    RecyclerView recyclerView;
    DatabaseReference database;
    FamilyMemberAdapter adapter;
    ArrayList<User> list;
    FirebaseAuth mAuth;
    Map<String, User> userMapping;
    String babyid;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_page);

        // get current babyid
        Intent intent = getIntent();
        babyid = intent.getStringExtra("babyid");
        Log.i("db-debug", "family page baby id: " + babyid);

        // get userid
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        family_add = findViewById(R.id.textView_family_add);
        family_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FamilySearchContainerActivity.class);
                intent.putExtra("babyid", babyid);
                Log.i("db-debug", "family page baby id: " + babyid + " add on click");
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView_family_member);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new FamilyMemberAdapter(this, list);
        recyclerView.setAdapter(adapter);

        // fetch all the users
        userMapping = new HashMap<>();
        database = FirebaseDatabase.getInstance().getReference("Users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userMapping.put(user.getId(), user);
                }
                for (String id : userMapping.keySet()) {
                    findFollowers(id);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // find users that follow current baby
    private void findFollowers(String id) {
        database = FirebaseDatabase.getInstance().getReference("Follow");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) { // check if user exists in Follow branch
                    // check if user follow current baby
                    database = FirebaseDatabase.getInstance().getReference("Follow/" + id);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(babyid).exists()) {
                                list.add(userMapping.get(id));
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}