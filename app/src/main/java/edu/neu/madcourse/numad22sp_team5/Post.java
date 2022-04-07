package edu.neu.madcourse.numad22sp_team5;

public class Post {
    String description;
    String growth;
    String postImages;
    String postType;
    String postid;
    String publisher;
    String tag;
    String time;

    public Post() {

    }

    public Post(String description, String growth, String postImages, String postType, String postid, String publisher, String tag, String time) {
        this.description = description;
        this.growth = growth;
        this.postImages = postImages;
        this.postType = postType;
        this.postid = postid;
        this.publisher = publisher;
        this.tag = tag;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public String getPostImages() {
        return postImages;
    }

    public void setPostImages(String postImages) {
        this.postImages = postImages;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
