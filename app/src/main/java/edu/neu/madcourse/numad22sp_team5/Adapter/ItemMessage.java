package edu.neu.madcourse.numad22sp_team5.Adapter;

public class ItemMessage {
    private final String babyId;
    private final String nickname;
    private final String headshot;
    private boolean notifyOnCreate = false;

    public ItemMessage(String babyId, String nickname, String headshot, boolean notifyOnCreate) {
        this.babyId = babyId;
        this.nickname = nickname;
        this.headshot = headshot;
        this.notifyOnCreate = notifyOnCreate;
    }

    public String getBabyId() {
        return babyId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadshot() {return headshot;}

    public boolean isNotifyOnCreate() {return notifyOnCreate;}
}
