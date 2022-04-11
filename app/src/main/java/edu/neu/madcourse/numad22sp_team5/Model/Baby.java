package edu.neu.madcourse.numad22sp_team5.Model;

public class Baby {
    public String babyid;
    public String nickname;
    public String dob;
    public String gender;
    public String headshot;
    public String ownerid;

    public Baby(String babyid,String ownerid, String nickname, String dob, String gender, String headshot) {
        this.babyid = babyid;
        this.ownerid = ownerid;
        this.nickname = nickname;
        this.dob = dob;
        this.gender = gender;
        this.headshot = headshot;
    }

    public Baby() {

    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getBabyid() {
        return babyid;
    }

    public void setBabyid(String babyid) {
        this.babyid = babyid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String birthday) {
        this.dob = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }
}
