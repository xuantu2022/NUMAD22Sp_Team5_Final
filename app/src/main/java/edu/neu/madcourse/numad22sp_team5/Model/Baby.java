package edu.neu.madcourse.numad22sp_team5.Model;

public class Baby {
    private String babyid;
    private String nickname;
    private String birthday;
    private String gender;
    private String headshot;

    public Baby(String babyid, String nickname, String birthday, String gender, String headshot) {
        this.babyid = babyid;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.headshot = headshot;
    }

    public Baby() {

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
