package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.numad22sp_team5.Adapter.BabyAdapter;
import edu.neu.madcourse.numad22sp_team5.Model.Baby;


public class BabyListActivity extends AppCompatActivity implements BabyAdapter.OnBabyListener {
    private RecyclerView babyView;
    private BabyAdapter babyAdapter;
    private List<Baby> babyList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_list);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        babyView = (RecyclerView) findViewById(R.id.baby_list);
        babyView.setHasFixedSize(true);
        babyView.setLayoutManager(mLayoutManager);

        babyList = new ArrayList<>();

        babyAdapter = new BabyAdapter(this, babyList, this);
        babyView.setAdapter(babyAdapter);

        initBabyData();

        mButton = findViewById(R.id.floatingActionButton);
    }

    private void initBabyData() {

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference babyIdRef = FirebaseDatabase.getInstance().getReference("Follow").child(userid);
        //get user's baby list
        babyIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                babyList.clear();
                for(DataSnapshot data: snapshot.getChildren()) {
                    String babyid = data.getKey();
                    //use babyid to get baby data.
                    getBabyInfo(babyid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getBabyInfo(String babyid) {
        DatabaseReference babyRef = FirebaseDatabase.getInstance().getReference().child("Babys").child(babyid);
        babyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Baby baby = snapshot.getValue(Baby.class);
                babyList.add(baby);
                babyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onClickFAB(View view) {
        openBabyDetail();
    }


    public void openBabyDetail() {
        Intent intent = new Intent(this, AddBabyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBabyClick(int position) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("babyid", babyList.get(position).getBabyid());
        intent.putExtra("headshot", babyList.get(position).getHeadshot());
        intent.putExtra("nickname",babyList.get(position).getNickname());
        startActivity(intent);
    }
}