package edu.neu.madcourse.numad22sp_team5;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.HashSet;

// helper class to parse a snapshot of the entire database. This class is thread-compatible.
public class SnapshotParser {
    // My user id.
    private String user = new String();

    private HashMap<String, Long> babyPostCounter = new HashMap<>();
    private HashMap<String, Long> babyCommentCounter = new HashMap<>();
    private HashMap<String, Long> babyLikeCounter = new HashMap<>();

    public SnapshotParser(String user) {
        this.user = user;
    }

    public Long postCountForBaby(String baby) {
        return babyPostCounter.get(baby);
    }

    public void setBabyPostCounter(HashMap<String, Long> newCounter) {
        this.babyPostCounter = newCounter;
    }

    // Parses the entire database snapshot, caches the number of posts, comments and likes for babies.
    public void parse(DataSnapshot snapshot) {
        babyPostCounter = parseBabyPostCount(snapshot);
    }

    // Returns a map between post id and baby id.
    private HashMap<String, String> postToBaby(DataSnapshot snapshot) {
        HashMap<String, String> postToBabyMap = new HashMap<>();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                postToBabyMap.put(post_snapshot.getKey().toString(), baby_id);
            }
        }
        return postToBabyMap;
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
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            counter.put(baby_id, baby_post_snapshot.getChildrenCount());
        }
        return counter;
    }

    public String publisherOfBabyLastPost(DataSnapshot snapshot, String baby) {
        String publisher = new String();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = baby_post_snapshot.getKey().toString();
            if (!baby_id.equals(baby)) { continue; }
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                publisher = post_snapshot.child("publisher").getValue().toString();
            }
        }
        return publisher;
    }

}
