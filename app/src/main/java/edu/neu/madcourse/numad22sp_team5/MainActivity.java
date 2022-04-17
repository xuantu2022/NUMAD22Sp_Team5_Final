package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

import edu.neu.madcourse.numad22sp_team5.fragment.HomeFragment;
import edu.neu.madcourse.numad22sp_team5.fragment.MessageFragment;
import edu.neu.madcourse.numad22sp_team5.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    //BottomNavigationView bottomNavigationView;
    NavigationBarView navigationBarView;
    Fragment selectedFragment = null;

    //add notification indicator
    public View notificationIndicator;


    private String babyid;
    private String headshot;
    private String nickname;
    private boolean onCreate = true;

    GlobalStatus globalStatus = (GlobalStatus) this.getApplication();
    private SnapshotParser snapshotParser;
    private HashSet<String> babyFollowed = new HashSet<>();
    private HashSet<String> postIDList = new HashSet<>();
    private HashMap<String, String> postIDToBabyID = new HashMap<>();
    private HashMap<String, Long> babyPostCount = new HashMap<>();
    private HashMap<String, Long> postCommentCount = new HashMap<>();
    private HashMap<String, Long> postLikeCount = new HashMap<>();
    private int commentCount = 0;
    private int likeCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //globalStatus.setTest("test");

        navigationBarView = (NavigationBarView) findViewById(R.id.bottom_navigation);
        /* disable customized toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/

        //get babyInfo from sharedPreferences

        SharedPreferences pref = getSharedPreferences("babyInfo",MODE_PRIVATE);
        babyid = pref.getString("babyid","");
        headshot = pref.getString("headshot", "");
        nickname = pref.getString("nickname", "");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        snapshotParser = new SnapshotParser(firebaseUser.getUid());
        /*
        Intent intent = getIntent();
        babyid = intent.getStringExtra("babyid");
        headshot = intent.getStringExtra("headshot");
        nickname = intent.getStringExtra("nickname");*/
//        Log.d("babyidmain", babyid);

        //enable back button on action bar and hide title
        /*
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        //bottom navigation bar selector
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        globalStatus.setIsInMessage(false);
                        break;
                    case R.id.nav_message:
                        selectedFragment = new MessageFragment();
                        globalStatus.setIsInMessage(true);
                        //add notification indicator
                        if(notificationIndicator != null){
                            if(notificationIndicator.getVisibility() == View.VISIBLE){
                                notificationIndicator.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case R.id.nav_setting:
                        selectedFragment = new SettingFragment();
                        globalStatus.setIsInMessage(false);
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment, "Selected").commit();
                }

                return true;
            }
        });

        //retrieve saved state
        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, "KEY");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        }  else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        NavigationBarView itemView = (NavigationBarView) findViewById(R.id.bottom_navigation);
        notificationIndicator = LayoutInflater.from(this).inflate(R.layout.layout_indicator,navigationBarView,false);
        notificationIndicator.setVisibility(View.GONE);
        itemView.addView(notificationIndicator);

        initNotificationIndicator();

        // showNotificationIndicator();
    }

    //Save current state of fragment
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Selected");
        if (fragment != null) {
            getSupportFragmentManager().putFragment(outState, "KEY", fragment);
        }
    }

    private void initNotificationIndicator() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (onCreate) {
                    snapshotParser.parse(snapshot);
                    onCreate = false;
                } else {
                    // check posts.
                    HashMap<String, Long> babyPostCount = snapshotParser.parseBabyPostCount(snapshot);
                    HashSet<String> babyFollowed = snapshotParser.parseFollowed(snapshot);
                    for (String baby : babyFollowed) {
                        if (babyPostCount.get(baby) > snapshotParser.postCountForBaby(baby)) {
                            if (snapshotParser.publisherOfBabyLastPost(snapshot, baby).equals(firebaseUser.getUid())) {
                                continue;
                            }
                            showNotificationIndicator(baby);
                        }
                    }
                    snapshotParser.setBabyPostCounter(babyPostCount);

                    // check comments
                }
//                if (onCreate) {
//                    Log.d("heng", "a new snapshot");
//                    babyFollowed.clear();
//                    for (DataSnapshot followSnapshot : snapshot.child("Follow").child(firebaseUser.getUid()).getChildren()) {
//                        boolean follow = (boolean) followSnapshot.getValue();
//                        if (follow) {
//                            Log.d("heng", "adding followed baby " + followSnapshot.getKey());
//                            babyFollowed.add(followSnapshot.getKey().toString());
//                        }
//                    }
//                    for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
//                        String baby_id = baby_post_snapshot.getKey().toString();
//                        if (!babyFollowed.contains(baby_id)) {
//                            continue;
//                        }
//                        babyPostCount.put(baby_id, baby_post_snapshot.getChildrenCount());
//                        Log.d("heng", "found posts for followed baby " + baby_id);
//                        for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
//                            postIDToBabyID.put(post_snapshot.getKey().toString(), baby_id);
//                            String publisher = post_snapshot.child("publisher").getValue().toString();
//                            Log.d("heng", "post snapshot id " + post_snapshot.getKey().toString() + " by publisher " + publisher);
//                            if (publisher.equals(firebaseUser.getUid())) {
//                                postIDList.add(post_snapshot.getKey().toString());
//                            }
//                        }
//                    }
//                    for (DataSnapshot comment_snapshot : snapshot.child("Comments").getChildren()) {
//                        String postID = comment_snapshot.getKey().toString();
//                        postCommentCount.put(postID, comment_snapshot.getChildrenCount());
//                    }
//                    for (DataSnapshot like_snapshot : snapshot.child("Likes").getChildren()) {
//                        String postID = like_snapshot.getKey().toString();
//                        postLikeCount.put(postID, like_snapshot.getChildrenCount());
//                    }
//                    onCreate = false;
//                } else {
//                    Log.d("heng", "a new snapshot");
//                    babyFollowed.clear();
//                    for (DataSnapshot followSnapshot : snapshot.child("Follow").child(firebaseUser.getUid()).getChildren()) {
//                        boolean follow = (boolean) followSnapshot.getValue();
//                        if (follow) {
//                            Log.d("heng", "adding followed baby " + followSnapshot.getKey());
//                            babyFollowed.add(followSnapshot.getKey().toString());
//                        }
//                    }
//                    for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
//                        String baby_id = baby_post_snapshot.getKey().toString();
//                        if (!babyFollowed.contains(baby_id)) {
//                            continue;
//                        }
//                        Log.d("heng", "found posts for followed baby " + baby_id);
//                        for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
//                            postIDToBabyID.put(post_snapshot.getKey().toString(), baby_id);
//                            String publisher = post_snapshot.child("publisher").getValue().toString();
//                            Log.d("heng", "post snapshot id " + post_snapshot.getKey().toString() + " by publisher " + publisher);
//                            if (publisher.equals(firebaseUser.getUid())) {
//                                postIDList.add(post_snapshot.getKey().toString());
//                            }
//                            if (!publisher.equals(firebaseUser.getUid())) {
//                                Log.d("heng", "about to show notification, oncreate is " + onCreate);
//                                showNotificationIndicator(baby_id);
//                            }
//                        }
//                    }
//                    for (DataSnapshot comment_snapshot : snapshot.child("Comments").getChildren()) {
//                        String postID = comment_snapshot.getKey().toString();
//                        String commentPublisher = comment_snapshot.child(postID).child("publisher").getValue().toString();
//                        if (postIDList.contains(postID) && !commentPublisher.equals(firebaseUser.getUid())) {
//                            showNotificationIndicator(postIDToBabyID.get(postID));
//                        }
//                    }
//                    for (DataSnapshot like_snapshot : snapshot.child("Likes").getChildren()) {
//                        String postID = like_snapshot.getKey().toString();
//                        String userId = like_snapshot.child(postID).getChildren().toString();
//                        if (postIDList.contains(postID) && !like_snapshot.equals(firebaseUser.getUid())) {
//                            showNotificationIndicator(postIDToBabyID.get(postID));
//                        }
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //add notification indicator
    private void showNotificationIndicator(String babyId) {
        if (!globalStatus.getIsInMessage()) {
            notificationIndicator.setVisibility(View.VISIBLE);
            globalStatus.addBabyNotify(babyId);
        }
    }

    /* disable camera on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.drawable.ic_back:
                Intent intent1 = new Intent(this,BabyListActivity.class);
                startActivity(intent1);
                return true;
            case R.id.add_items:
                Intent intent2 = new Intent(this,AddActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public String getBabyid() {
        return babyid;
    }

    public String getHeadshot() {
        return headshot;
    }

    public String getNickname() {
        return nickname;
    }
}

