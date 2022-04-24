package edu.neu.madcourse.numad22sp_team5.Support;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.HashSet;

public class NotificationParser {
    private String user = new String();
    private HashMap<String, Long> babyPostCounter = new HashMap<>();
    private boolean parsed = false;

    public NotificationParser(String user) {
        this.user = user;
    }

    // Parses the entire database snapshot, caches the number of notifications
    public void parse(DataSnapshot snapshot) {
        if (parsed) return;
        babyPostCounter = parseBabyPostCount(snapshot);
        parsed = true;
    }

    // Returns a set of children ids followed by this user.
    public HashSet<String> parseFollowed(DataSnapshot snapshot) {
        HashSet<String> followed = new HashSet<>();
        for (DataSnapshot followSnapshot : snapshot.child("Follow").child(this.user).getChildren()) {
            boolean follow = (boolean) followSnapshot.getValue();
            if (follow) {
                followed.add(followSnapshot.getKey().toString());
            }
        }
        return followed;
    }

    // Returns a map between baby id and the number of posts associated with it.
    public HashMap<String, Long> parseBabyPostCount(DataSnapshot snapshot) {
        HashMap<String, Long> counter = new HashMap<>();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Notification").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            counter.put(baby_id, baby_post_snapshot.getChildrenCount());
        }
        return counter;
    }

    public Long postCountForBaby(String baby) {
        if (!babyPostCounter.containsKey(baby)) {
            Log.d("database corruption", "post table not initialized for baby " + baby);
            return 0L;
        }
        return babyPostCounter.get(baby);
    }

    public String publisherOfBabyLastPost(DataSnapshot snapshot, String baby) {
        String publisher = new String();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Notification").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            if (!baby_id.equals(baby)) { continue; }
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                publisher = post_snapshot.child("publisher").getValue().toString();
            }
        }
        return publisher;
    }

    public String publisherOfPost(DataSnapshot snapshot, String baby) {
        String postPublisher = new String();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Notification").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            if (!baby_id.equals(baby)) { continue; }
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                postPublisher = post_snapshot.child("post publisher").getValue().toString();
            }
        }
        return postPublisher;
    }

    public String type(DataSnapshot snapshot, String baby) {
        String type = new String();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Notification").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            if (!baby_id.equals(baby)) { continue; }
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                type = post_snapshot.child("type").getValue().toString();
            }
        }
        return type;
    }

    public void setBabyPostCounter(HashMap<String, Long> newCounter) {
        this.babyPostCounter = newCounter;
    }
}
