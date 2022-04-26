package edu.neu.madcourse.numad22sp_team5.Model;

public class Message {
    private String description;
    private String postPublisher;
    private String postImage;
    private String postid;
    private String publisher;
    private String time;
    private String type;

    public Message(String description, String postPublisher, String postImage, String postid,
                        String publisher, String time, String type) {
        this.time = time;
        this.publisher = publisher;
        this.type = type;
        this.description = description;
        this.postid = postid;
        this.postImage = postImage;
        this.postPublisher = postPublisher;
    }

    public Message(){}

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
        return postid;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostPublisher() {return postPublisher;}
}
