package edu.neu.madcourse.numad22sp_team5;



public class Post {
    private String postid;
    private String publisher;
    private String tag;
    private String time;
    private String postType;
    private String description;
    private String postImages;
    private String growth;

    public Post(String postid, String publisher, String tag, String time, String description, String postImages, String growth, String postType) {
        this.postid = postid;
        this.publisher = publisher;
        this.tag = tag;
        this.growth = growth;
        this.time = time;
        this.description = description;
        this.postImages = postImages;
        this.postType = postType;
    }

    public Post(){

    }

    public String getPostType() {
        return postType;
    }

    public String getGrowth() {
        return growth;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostType(String postType) {
        this.postType = postType;
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


    public void setGrowth(String growth) {
        this.growth = growth;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostImages() {
        return postImages;
    }

    public void setPostImages(String postImages) {
        this.postImages = postImages;
    }

    @Override
    public String toString(){
        return this.postid  + "\n" +
        this.publisher +"\n" +
        this.tag +"\n" +
        this.growth +"\n" +
        this.time +"\n" +
        this.description +"\n" + this.postImages + "\n";
    }
}
