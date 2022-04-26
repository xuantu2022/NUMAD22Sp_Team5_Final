package edu.neu.madcourse.numad22sp_team5;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

// helper class to parse a snapshot of the entire database. This class is thread-compatible.
public class SnapshotParser {
    // My user id.
    private String user = new String();

    private HashMap<String, Long> babyPostCounter = new HashMap<>();
    private HashMap<String, Long> babyCommentCounter = new HashMap<>();
    private HashMap<String, Long> postCommentCounter = new HashMap<>();
    private HashMap<String, Long> babyLikeCounter = new HashMap<>();
    private HashMap<String, Long> postLikeCounter = new HashMap<>();
    private boolean parsed = false;

    public SnapshotParser(String user) {
        this.user = user;
    }

    public Long postCountForBaby(String baby) {
        if (!babyPostCounter.containsKey(baby)) {
            Log.d("database corruption", "post table not initialized for baby " + baby);
            return 0L;
        }
        return babyPostCounter.get(baby);
    }

    public Long commentCountForBaby(String baby) {
        if (!babyCommentCounter.containsKey(baby)) {
            Log.d("database corruption", "Comment table not initialized for baby " + baby);
            return 0L;
        }
        return babyCommentCounter.get(baby);
    }

    public Long commentCountForPost(String post) {
        if (!postCommentCounter.containsKey(post)) {
            Log.d("database corruption", "comment table not updated for post " + post);
            return 0L;
        }
        return postCommentCounter.get(post);
    }

    public Long likeCountForBaby(String baby) {
        if (!babyLikeCounter.containsKey(baby)) {
            Log.d("database corruption", "like table not updated for baby " + baby);
            return 0L;
        }
        return babyLikeCounter.get(baby);
    }

    public Long likeCountForPost(String post) {
        if (!postLikeCounter.containsKey(post)) {
            Log.d("database corruption", "Like table not updated for post " + post);
            return 0L;
        }
        return postLikeCounter.get(post);
    }

    public void setBabyPostCounter(HashMap<String, Long> newCounter) {
        this.babyPostCounter = newCounter;
    }

    public void setPostCountForBaby(String baby, Long count) {
        this.babyPostCounter.put(baby, count);
    }

    public void setBabyCommentCounter(HashMap<String, Long> newCounter) {
        this.babyCommentCounter = newCounter;
    }

    public void setCommentForBaby(String baby, Long count) {
        this.babyCommentCounter.put(baby, count);
    }

    public void setPostCommentCounter(HashMap<String, Long> newCounter) {
        this.postCommentCounter = newCounter;
    }

    public void setCommentForPost(String post, Long count) {
        this.postCommentCounter.put(post, count);
    }

    public void setBabyLikeCounter(HashMap<String, Long> newCounter) {
        this.babyLikeCounter = newCounter;
    }

    public void setLikeForBaby(String baby, Long count) {
        this.babyLikeCounter.put(baby, count);
    }

    public void setPostLikeCounter(HashMap<String, Long> newCounter) {
        this.postLikeCounter = newCounter;
    }

    public void setLikeForPost(String post, Long count) {
        this.postLikeCounter.put(post, count);
    }

    // Parses the entire database snapshot, caches the number of posts, comments and likes for babies.
    public void parse(DataSnapshot snapshot) {
        if (parsed) return;
        babyPostCounter = parseBabyPostCount(snapshot);
        babyCommentCounter = parseBabyCommentCount(snapshot);
        postCommentCounter = parsePostCommentCount(snapshot);
        babyLikeCounter = parseBabyLikeCount(snapshot);
        postLikeCounter = parsePostLikeCount(snapshot);
        parsed = true;
    }

    // Returns a map between post id and baby id.
    private HashMap<String, String> postToBaby(DataSnapshot snapshot) {
        HashMap<String, String> postToBabyMap = new HashMap<>();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = Objects.requireNonNull(baby_post_snapshot.getKey()).toString();
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                postToBabyMap.put(Objects.requireNonNull(post_snapshot.getKey()).toString(), baby_id);
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
                followed.add(Objects.requireNonNull(followSnapshot.getKey()).toString());
            }
        }
        return followed;
    }

    public HashSet<String> myPosts(DataSnapshot snapshot) {
        HashSet<String> posts = new HashSet<>();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = Objects.requireNonNull(baby_post_snapshot.getKey()).toString();
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                String postId = Objects.requireNonNull(post_snapshot.getKey()).toString();
                String publisher = Objects.requireNonNull(post_snapshot.child("publisher").getValue()).toString();
                if (publisher.equals(user)) {
                    posts.add(postId);
                }
            }
        }
        return posts;
    }

    // Returns a map between baby id and the number of posts associated with it.
    public HashMap<String, Long> parseBabyPostCount(DataSnapshot snapshot) {
        HashMap<String, Long> counter = new HashMap<>();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = Objects.requireNonNull(baby_post_snapshot.getKey()).toString();
            counter.put(baby_id, baby_post_snapshot.getChildrenCount());
        }
        return counter;
    }

    public HashMap<String, Long> parseBabyCommentCount(DataSnapshot snapshot) {
        HashMap<String, Long> counter = new HashMap<>();
        HashSet<String> babies = parseFollowed(snapshot);
        for (String baby : babies) {
            counter.put(baby, 0L);
        }
        HashMap<String, String> postToBaby = postToBaby(snapshot);
        for (DataSnapshot commentSnapshot : snapshot.child("Comments").getChildren()) {
            String postId = Objects.requireNonNull(commentSnapshot.getKey()).toString();
            String babyId = postToBaby.get(postId);
            if (!babies.contains(babyId)) continue;
            long cur = Objects.requireNonNull(counter.get(babyId));
            cur = cur + commentSnapshot.getChildrenCount();
            counter.put(babyId, cur);
        }
        return counter;
    }

    public HashMap<String, Long> parsePostCommentCount(DataSnapshot snapshot) {
        HashMap<String, Long> counter = new HashMap<>();
        for (DataSnapshot commentSnapshot : snapshot.child("Comments").getChildren()) {
            String postId = Objects.requireNonNull(commentSnapshot.getKey()).toString();
            counter.put(postId, commentSnapshot.getChildrenCount());
        }
        return counter;
    }

    public HashMap<String, Long> parseBabyLikeCount(DataSnapshot snapshot) {
        HashMap<String, Long> counter = new HashMap<>();
        HashSet<String> babies = parseFollowed(snapshot);
        for (String baby : babies) {
            counter.put(baby, 0L);
        }
        HashMap<String, String> postToBaby = postToBaby(snapshot);
        for (DataSnapshot likeSnapshot : snapshot.child("Likes").getChildren()) {
            String postId = Objects.requireNonNull(likeSnapshot.getKey()).toString();
            String babyId = postToBaby.get(postId);
            if (!babies.contains(babyId)) continue;
            long cur = Objects.requireNonNull(counter.get(babyId));
            cur = cur + likeSnapshot.getChildrenCount();
            counter.put(babyId, cur);
        }
        return counter;
    }

    public HashMap<String, Long> parsePostLikeCount(DataSnapshot snapshot) {
        HashMap<String, Long> counter = new HashMap<>();
        for (DataSnapshot likeSnapshot : snapshot.child("Likes").getChildren()) {
            String postId = Objects.requireNonNull(likeSnapshot.getKey()).toString();
            counter.put(postId, likeSnapshot.getChildrenCount());
        }
        return counter;
    }

    public String publisherOfBabyLastPost(DataSnapshot snapshot, String baby) {
        String publisher = new String();
        for (DataSnapshot baby_post_snapshot : snapshot.child("Posts").getChildren()) {
            String baby_id = Objects.requireNonNull(baby_post_snapshot.getKey()).toString();
            if (!baby_id.equals(baby)) { continue; }
            for (DataSnapshot post_snapshot : baby_post_snapshot.getChildren()) {
                publisher = Objects.requireNonNull(post_snapshot.child("publisher").getValue()).toString();
            }
        }
        return publisher;
    }

    public String publisherOfLastCommentOnPost(DataSnapshot snapshot, String postId) {
        String publisher = new String();
        for (DataSnapshot comment_snapshot : snapshot.child("Comments").child(postId).getChildren()) {
            publisher = Objects.requireNonNull(comment_snapshot.child("publisher").getValue()).toString();
        }
        return publisher;
    }

    public String publisherOfLastLikeOnPost(DataSnapshot snapshot, String postId) {
        String publisher = new String();
        for (DataSnapshot like_snapshot : snapshot.child("Likes").child(postId).getChildren()) {
            publisher = Objects.requireNonNull(like_snapshot.getKey()).toString();
        }
        return publisher;
    }
}
