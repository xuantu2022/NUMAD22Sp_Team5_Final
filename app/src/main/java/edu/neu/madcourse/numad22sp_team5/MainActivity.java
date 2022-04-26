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
import java.util.Objects;
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
//    private HashSet<String> babyFollowed = new HashSet<>();
//    private HashSet<String> postIDList = new HashSet<>();
//    private HashMap<String, String> postIDToBabyID = new HashMap<>();
//    private HashMap<String, Long> babyPostCount = new HashMap<>();
//    private HashMap<String, Long> postCommentCount = new HashMap<>();
//    private HashMap<String, Long> postLikeCount = new HashMap<>();
//    private int commentCount = 0;
//    private int likeCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreate = true;
        globalStatus.setIsInMessage(false);
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
        snapshotParser = new SnapshotParser(Objects.requireNonNull(firebaseUser).getUid());
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
                    Objects.requireNonNull(fragment)).commit();
        }  else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        NavigationBarView itemView = (NavigationBarView) findViewById(R.id.bottom_navigation);
        notificationIndicator = LayoutInflater.from(this).inflate(R.layout.layout_indicator,navigationBarView,false);
        notificationIndicator.setVisibility(View.GONE);
        itemView.addView(notificationIndicator);
        initNotificationIndicator();
    }

    //Save current state of fragment
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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
                    // check if there is any new post for followed babies.
                    HashMap<String, Long> babyPostCount = snapshotParser.parseBabyPostCount(snapshot);
                    //HashSet<String> babyFollowed = snapshotParser.parseFollowed(snapshot);
                    if (babyPostCount.containsKey(babyid)) {
                        if (babyPostCount.get(babyid) > snapshotParser.postCountForBaby(babyid)) {
                            if (!snapshotParser.publisherOfBabyLastPost(snapshot, babyid).equals(Objects.requireNonNull(firebaseUser).getUid())) {
                                showNotificationIndicator(babyid);
                            }
                        }
                    }
                    snapshotParser.setBabyPostCounter(babyPostCount);

                    // check if there is any new comments for my posts.
                    HashMap<String, Long> babyCommentCount = snapshotParser.parseBabyCommentCount(snapshot);
                    HashMap<String, Long> postCommentCount = snapshotParser.parsePostCommentCount(snapshot);
                    if (babyCommentCount.containsKey(babyid)) {
                        if (babyCommentCount.get(babyid) > snapshotParser.commentCountForBaby(babyid)) {
                            for (String myPost : snapshotParser.myPosts(snapshot)) {
                                if (!postCommentCount.containsKey(myPost)) {
                                    continue;
                                }
                                if (postCommentCount.get(myPost) > snapshotParser.commentCountForPost(myPost)) {
                                    if (!snapshotParser.publisherOfLastCommentOnPost(snapshot, myPost).equals(Objects.requireNonNull(firebaseUser).getUid())) {
                                        showNotificationIndicator(babyid);
                                    }
                                }
                            }
                        }
                    }
                    snapshotParser.setBabyCommentCounter(babyCommentCount);
                    snapshotParser.setPostCommentCounter(postCommentCount);

                    // Check if there is any new like for my posts.
                    HashMap<String, Long> babyLikeCount = snapshotParser.parseBabyLikeCount(snapshot);
                    HashMap<String, Long> postLikeCount = snapshotParser.parsePostLikeCount(snapshot);
                    if (babyLikeCount.containsKey(babyid)) {
                        if (babyLikeCount.get(babyid) > snapshotParser.likeCountForBaby(babyid)) {
                            for (String myPost : snapshotParser.myPosts(snapshot)) {
                                if (!postLikeCount.containsKey(myPost)) {
                                    continue;
                                }
                                if (postLikeCount.get(myPost) > snapshotParser.likeCountForPost(myPost)) {
                                    if (!snapshotParser.publisherOfLastLikeOnPost(snapshot, myPost).equals(Objects.requireNonNull(firebaseUser).getUid())) {
                                        showNotificationIndicator(babyid);
                                    }
                                }
                            }
                        }
                    }
                    snapshotParser.setBabyLikeCounter(babyLikeCount);
                    snapshotParser.setPostLikeCounter(postLikeCount);
                }
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
            if (!globalStatus.isMessageRunning()) {
                globalStatus.addBabyNotify(babyId);
            }
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

