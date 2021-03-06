package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.Map;
import java.util.Objects;

import edu.neu.madcourse.numad22sp_team5.Model.User;

public class FamilyPageActivity extends AppCompatActivity {

    TextView family_add;
    TextView parent_name;
    TextView parent_email;
    TextView parent_me;
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
//        Intent intent = getIntent();
//        babyid = intent.getStringExtra("babyid");

        SharedPreferences babyPref = getSharedPreferences("babyInfo", MODE_PRIVATE);
        babyid = babyPref.getString("babyid", "");

        // get userid
        mAuth = FirebaseAuth.getInstance();
        userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        family_add = findViewById(R.id.textView_family_add);
        parent_name = findViewById(R.id.textView_parentName);
        parent_email = findViewById(R.id.textView_parentEmail);
        parent_me = findViewById(R.id.textView_parent_me);

        // fetch all the users
        userMapping = new HashMap<>();
        database = FirebaseDatabase.getInstance().getReference("Users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userMapping.put(Objects.requireNonNull(user).getId(), user);
                }

                // check if current user is baby owner, if not, add button will be gone
                database = FirebaseDatabase.getInstance().getReference("Babys/" + babyid).child("ownerid");
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String ownerid = (String) snapshot.getValue();
                        if (Objects.equals(ownerid, userid)){
                            parent_me.setVisibility(View.VISIBLE);
                        }
                        if (!Objects.equals(ownerid, userid)) {
                            family_add.setVisibility(View.GONE);
                        }
                        // set parent information
                        parent_name.setText(Objects.requireNonNull(userMapping.get(ownerid)).getUsername());
                        parent_email.setText(Objects.requireNonNull(userMapping.get(ownerid)).getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        family_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FamilySearchContainerActivity.class);
                //intent.putExtra("babyid", babyid);
                //Log.i("db-debug", "family page baby id: " + babyid + " add on click");
                startActivity(intent);
            }
        });

        displayFamilyMembers();
    }

    // update recyclerview when add family members and back to family page
    @Override
    protected void onRestart() {
        super.onRestart();
        displayFamilyMembers();
    }

    private void displayFamilyMembers() {
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
                    userMapping.put(Objects.requireNonNull(user).getId(), user);
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
                list.clear();
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