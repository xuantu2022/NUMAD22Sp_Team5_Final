package edu.neu.madcourse.numad22sp_team5.Adapter;

public class ItemEvent {
    private String babyid;
    private String time;
    private String publisher;
    private String type;
    private String description;
    private String postId;

    public ItemEvent(String babyid, String time, String publisher, String type, String description, String postId) {
        this.babyid = babyid;
        this.time = time;
        this.publisher = publisher;
        this.type = type;
        this.description = description;
        this.postId = postId;
    }

    public String getBabyid() {
        return babyid;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getPostId() {
        return postId;
    }
}
