package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;

import edu.neu.madcourse.numad22sp_team5.Adapter.BabyAdapter;
import edu.neu.madcourse.numad22sp_team5.Model.Baby;


public class BabyListActivity extends AppCompatActivity implements BabyAdapter.OnBabyListener {
    private RecyclerView babyView;
    private BabyAdapter babyAdapter;
    private List<Baby> babyList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mButton;

    // add notification
    private boolean onCreate = true;
    GlobalStatus globalStatus = (GlobalStatus) this.getApplication();
    private SnapshotParser snapshotParser;
    private ArrayList<String> babyFollowed = new ArrayList<>();
    private HashMap<String, Long> babyNotifyCount = new HashMap<>();

    // Create Notification Channel
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Name";
            String description = "Notification Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    int noti_id = 1;
    //Send Notification
    public void sendNotification(String username) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);

        Notification noti = new NotificationCompat.Builder(this, "123")
                .setSmallIcon(R.drawable.badge_file)
                .setContentTitle("New Sticker from " + username)
                //.setLargeIcon(bitmap)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(noti_id++, noti);
    }


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

        //add notification
        createNotificationChannel();

        Log.d("babylist_notify", "start initNotification function");
        initNotificationIndicator();
        Log.d("babylist_notify", "end initNotification function");
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
        //use sharedPreference to pass value

        SharedPreferences.Editor editor = getSharedPreferences("babyInfo", MODE_PRIVATE).edit();
        editor.putString("babyid", babyList.get(position).getBabyid());
        editor.putString("headshot", babyList.get(position).getHeadshot());
        editor.putString("nickname", babyList.get(position).getNickname());
        editor.commit();

        //use intent to pass data

        Intent intent = new Intent(this, MainActivity.class);
        /*
        intent.putExtra("babyid", babyList.get(position).getBabyid());
        intent.putExtra("headshot", babyList.get(position).getHeadshot());
        intent.putExtra("nickname",babyList.get(position).getNickname());*/
        startActivity(intent);
    }

    private void initNotificationIndicator() {
        Log.d("babylist_notify", "into initNotification function");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get the followed baby's ids
                babyFollowed.clear();
                for (DataSnapshot data : snapshot.child("Follow").child(currentUserId).getChildren()) {
                    String babyid = data.getKey();
                    babyFollowed.add(babyid);
                }
                for (String babyID : babyFollowed) {
                    long counter = snapshot.child("Notification").child(babyID).getChildrenCount();
                    Log.d("babylist_notify", "counter is " + counter);
                    babyNotifyCount.put(babyID, counter);

                    for (DataSnapshot data : snapshot.child("Notification").child(babyID).getChildren()) {
                        String notifyId = data.getKey().toString();
                        String postPublisher = data.child("post publisher").getValue().toString();
                        String publisher = data.child("publisher").getValue().toString();
                        String type = data.child("type").getValue().toString();

                        if (publisher.equals(currentUserId)) {
                            continue;
                        }
                        if (type.equals("post")) {
                            sendNotification(publisher);
                            //globalStatus.addBabyListNotify(babyid);
                        }
                        if (type.equals("comment")) {
                            if (postPublisher.equals(currentUserId)) {
                                sendNotification(publisher);
                                //globalStatus.addBabyListNotify(babyid);
                            }
                        }
                        if (type.equals("like")) {
                            if (postPublisher.equals(currentUserId)) {
                                sendNotification(publisher);
                                //globalStatus.addBabyListNotify(babyid);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}