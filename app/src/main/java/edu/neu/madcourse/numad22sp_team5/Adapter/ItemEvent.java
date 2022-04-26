package edu.neu.madcourse.numad22sp_team5.Adapter;

public class ItemEvent {
    private String babyid;
    private String time;
    private String postPublisher;
    private String publisher;
    private String publisherName;
    private String type;
    private String description;
    private String postId;
    private String postImage;

    public ItemEvent(String babyid, String publisherName, String time, String publisher,
                     String type, String description, String postId, String postPublisher,String postImage) {
        this.babyid = babyid;
        this.time = time;
        this.publisher = publisher;
        this.publisherName = publisherName;
        this.type = type;
        this.description = description;
        this.postId = postId;
        this.postImage = postImage;
        this.postPublisher = postPublisher;
    }

    public ItemEvent(){}

    public String getBabyid() {
        return babyid;
    }

    public String getPublisherName() {
        return publisherName;
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

    public String getPostImage() {
        return postImage;
    }

    public String getPostPublisher() {return postPublisher;}
}
