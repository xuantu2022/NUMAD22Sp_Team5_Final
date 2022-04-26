package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import edu.neu.madcourse.numad22sp_team5.Adapter.BabyAdapter;
import edu.neu.madcourse.numad22sp_team5.Model.Baby;
import edu.neu.madcourse.numad22sp_team5.Model.Message;
import edu.neu.madcourse.numad22sp_team5.Model.Post;



public class BabyListActivity extends AppCompatActivity implements BabyAdapter.OnBabyListener {
    private RecyclerView babyView;
    private BabyAdapter babyAdapter;
    private List<Baby> babyList;
    private HashSet<String> babySet;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mButton;

    private int noti_id = 1;
    private final String channelId = "POST_BG";
    private boolean listenerCreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_list);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        babyView = (RecyclerView) findViewById(R.id.baby_list);
        babyView.setHasFixedSize(true);
        babyView.setLayoutManager(mLayoutManager);

        babyList = new ArrayList<>();
        babySet = new HashSet<>();

        babyAdapter = new BabyAdapter(this, babyList, this);
        babyView.setAdapter(babyAdapter);

        listenerCreated =  false;

        initBabyData();
        createNotificationChannel();
        listenerCreated = true;

        mButton = findViewById(R.id.floatingActionButton);
    }

    private void initBabyData() {

        String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference babyIdRef = FirebaseDatabase.getInstance().getReference("Follow").child(userid);
        //get user's baby list
        babyIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                babyList.clear();
                for(DataSnapshot data: snapshot.getChildren()) {
                    String babyid = data.getKey();
                    //Log.d("babyid", babyid);
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

                //Log.d("babyid in size ", ""+ babyList.size());
                babyAdapter.notifyDataSetChanged();

                //Xuan: add baby id to baby set for notification usage if not existing

                if (!babySet.contains(babyid) ) {
                    babySet.add(babyid);
                    initListener(babyid, baby.getNickname());
                }
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

    //Xuan: create change data listener
    public void initListener(String id, String nickname) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(id);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Post post = snapshot.getValue(Post.class);

                // Xuan: Only post notification when app running in background
                if (!isAppOnForeground()) {
                    // Xuan: check if lastPost is published by myself
                    String curUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    if (listenerCreated && post != null && !curUser.equals(post.getPublisher())) {
                        // Xuan: post a notification here
                        sendNotification(nickname);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    //add comment and like to trigger notification
//    public void initListener(String id, String nickname) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
//                .child("Notification").child(id);
//
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
//                Message notification = snapshot.getValue(edu.neu.madcourse.numad22sp_team5.Model.Message.class);
//
//                // Xuan: Only post notification when app running in background
//                if (!isAppOnForeground()) {
//                    // Xuan: check if lastPost is published by myself
//                    String curUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//                    if (listenerCreated && notification != null && !curUser.equals(notification.getPublisher())) {
//                        Log.d("info", "current user " + curUser);
//                        Log.d("info", "publisher " + notification.getPublisher());
//                        Log.d("info", "description  " + notification.getDescription());
//                        Log.d("info", "post id  " + notification.getPostId());
//                        Log.d("info", "post publisher " + notification.getPostPublisher());
//                        Log.d("info", "type " + notification.getType());
//                        if (notification.getType().equals("post")) {
//                            Log.d("info", "new post");
//                            sendNotification(nickname);
//                        }
//                        if (notification.getType().equals("comment") && curUser.equals(notification.getPostPublisher())) {
//                            Log.d("info", "new comment");
//                            sendNotification(nickname);
//                        }
//                        if (notification.getType().equals("like") && curUser.equals(notification.getPostPublisher())) {
//                            Log.d("info", "new like");
//                            sendNotification(nickname);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    // Xuan: Create Notification Channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Name";
            String description = "Notification Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Xuan: Send Notification
    private void sendNotification(String nickname) {
        Notification noti = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("New update for baby " + nickname)
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(noti_id++, noti);
    }

    // Xuan: Check app running on foreground
    private boolean isAppOnForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = manager.getRunningAppProcesses();

        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo p : appProcesses) {
            if (p.processName.equals(packageName)
                    && p.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(false);
    }
}