package edu.neu.madcourse.numad22sp_team5;

public class User {
    String email;
    String userid;
    String username;

    public User() {

    }

    public User(String email, String userid, String username) {
        this.email = email;
        this.userid = userid;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
