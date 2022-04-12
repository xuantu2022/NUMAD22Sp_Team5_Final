package edu.neu.madcourse.numad22sp_team5.Model;

public class User {
    public String id;
    public String email;
    public String username;

    public User(String userid, String email, String username) {
        this.id = userid;
        this.email = email;
        this.username = username;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
